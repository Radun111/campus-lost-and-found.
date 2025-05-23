package com.example.lostandfound.service;

import com.example.lostandfound.dto.ItemRequest;
import com.example.lostandfound.dto.ItemResponse;
import com.example.lostandfound.entity.*;
import com.example.lostandfound.repository.ItemRepository;
import com.example.lostandfound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;  // Fixed: Removed underscore
    private final UserRepository userRepository; // Fixed: Removed underscore

    public ItemResponse createItem(ItemRequest request) {  // Fixed: Corrected method name
        User reporter = userRepository.findById(request.getReportedById())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setLocationFound(request.getLocationFound());
        item.setStatus(ItemStatus.valueOf(request.getStatus())); // Fixed: Correct enum name
        item.setReportedBy(reporter);

        Item savedItem = itemRepository.save(item);
        return mapToResponse(savedItem); // Fixed: Added mapping method
    }

    private ItemResponse mapToResponse(Item item) {
        ItemResponse response = new ItemResponse();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setDescription(item.getDescription());
        response.setLocationFound(item.getLocationFound());
        response.setStatus(item.getStatus().name());
        response.setReportedBy(item.getReportedBy().getUsername());
        return response;
    }
}