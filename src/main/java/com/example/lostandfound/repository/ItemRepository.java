package com.example.lostandfound.repository;

import com.example.lostandfound.entity.Item;
import com.example.lostandfound.entity.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByStatus(ItemStatus status); // Finds items by status
    List<Item> findByReportedBy(User user); // Finds items by reporter

    @Query("SELECT i FROM Item i WHERE " +
            "(:#{#criteria.status} IS NULL OR i.status = :#{#criteria.status}) AND " +
            "(:#{#criteria.location} IS NULL OR i.locationFound LIKE %:#{#criteria.location}%) AND " +
            "(:#{#criteria.nameContains} IS NULL OR i.name LIKE %:#{#criteria.nameContains}%)")
    List<Item> searchItems(@Param("criteria") ItemSearchCriteria criteria);

}