package com.example.lostandfound.controller;

import com.example.lostandfound.dto.RequestDto;
import com.example.lostandfound.dto.RequestResponse;
import com.example.lostandfound.entity.RequestStatus;
import com.example.lostandfound.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {

    // This single field will automatically generate the constructor via Lombok
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestResponse createRequest(@RequestBody RequestDto requestDto) {
        return requestService.createRequest(requestDto);
    }

    @PatchMapping("/{requestId}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public RequestResponse approveRequest(@PathVariable Long requestId) {
        return requestService.updateRequestStatus(requestId, RequestStatus.APPROVED);
    }



    @PatchMapping("/{requestId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public RequestResponse rejectRequest(@PathVariable Long requestId) {
        return requestService.updateRequestStatus(requestId, RequestStatus.REJECTED);
    }

    @GetMapping("/{requestId}")
    public RequestResponse getRequest(@PathVariable Long requestId) {
        return requestService.getRequestById(requestId);
    }
}