package com.example.lostandfound.service;

import com.example.lostandfound.dto.RequestDto;
import com.example.lostandfound.dto.RequestResponse;
import com.example.lostandfound.entity.*;
import com.example.lostandfound.exception.ResourceNotFoundException;
import com.example.lostandfound.repository.ItemRepository;
import com.example.lostandfound.repository.RequestRepository;
import com.example.lostandfound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestService {

    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RequestResponse createRequest(RequestDto requestDto) {
        // Fetch authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User requester = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        // Validate item exists and is not already claimed
        Item item = itemRepository.findById(requestDto.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + requestDto.getItemId()));

        if (item.getStatus() == ItemStatus.CLAIMED) {
            throw new IllegalStateException("Item already claimed");
        }

        // Create and save request
        Request request = Request.builder()
                .item(item)
                .requester(requester)
                .status(RequestStatus.PENDING)
                .requestDate(LocalDateTime.now())
                .build();

        Request savedRequest = requestRepository.save(request);
        return mapToResponse(savedRequest);
    }

    public RequestResponse updateRequestStatus(Long requestId, RequestStatus newStatus) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));

        request.setStatus(newStatus);

        // If approved, mark item as claimed and notify requester
        if (newStatus == RequestStatus.APPROVED) {
            request.getItem().setStatus(ItemStatus.CLAIMED);
            emailService.sendEmail(
                    request.getRequester().getEmail(),
                    "Claim Approved",
                    "Your claim for '" + request.getItem().getName() + "' has been approved!"
            );
        }

        return mapToResponse(requestRepository.save(request));
    }

    public RequestResponse getRequestById(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));
        return mapToResponse(request);
    }

    private RequestResponse mapToResponse(Request request) {
        return RequestResponse.builder()
                .id(request.getId())
                .itemName(request.getItem().getName())
                .requesterName(request.getRequester().getUsername())
                .requestDate(request.getRequestDate())
                .status(request.getStatus().name())
                .build();
    }
}
