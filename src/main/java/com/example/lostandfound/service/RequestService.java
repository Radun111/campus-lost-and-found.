package com.example.lostandfound.service;

import com.example.lostandfound.dto.RequestDto;
import com.example.lostandfound.dto.RequestResponse;
import com.example.lostandfound.entity.*;
import com.example.lostandfound.exception.ResourceNotFoundException;
import com.example.lostandfound.repository.ItemRepository;
import com.example.lostandfound.repository.RequestRepository;
import com.example.lostandfound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    public RequestResponse createRequest(RequestDto requestDto) {
        // Validate item exists and is not already claimed
        Item item = itemRepository.findById(requestDto.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + requestDto.getItemId()));

        if (item.getStatus() == ItemStatus.CLAIMED) {
            throw new IllegalStateException("Item is already claimed");
        }

        // Validate user exists
        User requester = userRepository.findById(requestDto.getRequesterId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + requestDto.getRequesterId()));

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

        // Validate status transition
        if (request.getStatus() == RequestStatus.APPROVED && newStatus != RequestStatus.APPROVED) {
            throw new IllegalStateException("Cannot modify an approved request");
        }

        request.setStatus(newStatus);

        // Update item status if approved
        if (newStatus == RequestStatus.APPROVED) {
            Item item = request.getItem();
            item.setStatus(ItemStatus.CLAIMED);
            itemRepository.save(item);

            // Send approval email
            emailService.sendEmail(
                    request.getRequester().getEmail(),
                    "Claim Approved",
                    "Your claim for " + item.getName() + " has been approved!"
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