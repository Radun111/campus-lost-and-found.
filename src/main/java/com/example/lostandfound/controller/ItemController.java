package com.example.lostandfound.controller;

import com.example.lostandfound.dto.ItemRequest;
import com.example.lostandfound.dto.ItemResponse;
import com.example.lostandfound.dto.ItemSearchCriteria;
import com.example.lostandfound.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse createItem(@RequestBody ItemRequest request) {
        return itemService.createItem(request);
    }

    @GetMapping("/{id}")
    public ItemResponse getItem(@PathVariable Long id) {
        return itemService.getItemById(id);
    }


    @PostMapping("/search")
    public List<ItemResponse> searchItems(@RequestBody ItemSearchCriteria criteria) {
        return itemService.searchItems(criteria);
    }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')") // Or "hasAnyRole('ADMIN', 'STAFF')" if needed
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void deleteItem(@PathVariable Long id) {
            itemService.deleteItem(id);
        }

    }
