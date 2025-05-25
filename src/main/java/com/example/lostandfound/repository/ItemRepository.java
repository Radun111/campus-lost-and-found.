package com.example.lostandfound.repository;

import com.example.lostandfound.entity.Item;
import com.example.lostandfound.dto.ItemSearchCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE " +
            "(:#{#criteria.status} IS NULL OR i.status = :#{#criteria.status}) AND " +
            "(:#{#criteria.location} IS NULL OR i.locationFound LIKE %:#{#criteria.location}%) AND " +
            "(:#{#criteria.nameContains} IS NULL OR i.name LIKE %:#{#criteria.nameContains}%)")
    List<Item> searchItems(@Param("criteria") ItemSearchCriteria criteria);
}