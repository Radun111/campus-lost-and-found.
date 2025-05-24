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

    public RequestResponse createRequest(RequestDto requestDto) {
        // 1. Validate item exists
        Item item = itemRepository.findById(requestDto.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + requestDto.getItemId()));

        // 2. Validate user exists
        User requester = userRepository.findById(requestDto.getRequesterId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + requestDto.getRequesterId()));

        // 3. Create and save request
        Request request = new Request();
        request.setItem(item);
        request.setRequester(requester);
        request.setStatus(RequestStatus.PENDING); // Default status
        request.setRequestDate(LocalDateTime.now()); // Auto timestamp

        Request savedRequest = requestRepository.save(request);

        // 4. Return formatted response
        return mapToResponse(savedRequest);
    }

    public RequestResponse updateRequestStatus(Long requestId, RequestStatus newStatus) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));

        // Additional validation could be added here (e.g., only ADMIN can approve)
        request.setStatus(newStatus);

        // If approved, update item status to CLAIMED
        if (newStatus == RequestStatus.APPROVED) {
            request.getItem().setStatus(ItemStatus.CLAIMED);
        }

        return mapToResponse(requestRepository.save(request));
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