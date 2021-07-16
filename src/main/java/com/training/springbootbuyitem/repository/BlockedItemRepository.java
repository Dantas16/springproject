package com.training.springbootbuyitem.repository;

import com.training.springbootbuyitem.entity.model.BlockedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockedItemRepository extends JpaRepository<BlockedItem, Long> {
}
