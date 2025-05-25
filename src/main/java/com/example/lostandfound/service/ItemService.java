package com.example.lostandfound.service;

import com.example.lostandfound.dto.ItemRequest;
import com.example.lostandfound.dto.ItemResponse;
import com.example.lostandfound.dto.ItemSearchCriteria;
import com.example.lostandfound.entity.*;
import com.example.lostandfound.exception.ResourceNotFoundException;
import com.example.lostandfound.repository.ItemRepository;
import com.example.lostandfound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemResponse getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        return convertToResponse(item);
    }

    public ItemResponse createItem(ItemRequest request) {
        User reporter = userRepository.findById(request.getReportedById())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getReportedById()));

        Item item = Item.builder()
                .name(request.getName())
                .description(request.getDescription())
                .locationFound(request.getLocationFound())
                .status(ItemStatus.valueOf(request.getStatus()))
                .reportedBy(reporter)
                .build();

        return convertToResponse(itemRepository.save(item));
    }

    public List<ItemResponse> searchItems(ItemSearchCriteria criteria) {
        return itemRepository.searchItems(criteria).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private ItemResponse convertToResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .locationFound(item.getLocationFound())
                .status(item.getStatus().name())
                .reportedBy(item.getReportedBy().getUsername())
                .build();
    }
}