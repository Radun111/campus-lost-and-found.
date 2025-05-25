package com.example.lostandfound.service;

import com.example.lostandfound.dto.ItemSearchCriteria;
import com.example.lostandfound.entity.ItemStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceTest {
    @Autowired
    private ItemService itemService;

    @Test
    void searchItems_ShouldFilterByStatus() {
        ItemSearchCriteria criteria = new ItemSearchCriteria();
        criteria.setStatus(ItemStatus.FOUND);

        var results = itemService.searchItems(criteria);
        assertTrue(results.stream().allMatch(
                i -> i.getStatus().equals("FOUND")
        ));
    }
}