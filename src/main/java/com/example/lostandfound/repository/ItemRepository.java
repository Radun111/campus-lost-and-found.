package com.example.lostandfound.repository;

import com.example.lostandfound.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByStatus(ItemStatus status); // Finds items by status
    List<Item> findByReportedBy(User user); // Finds items by reporter
}