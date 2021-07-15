package com.training.springbootbuyitem.repository;

import com.training.springbootbuyitem.entity.model.BlockedItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedItemRepository extends JpaRepository<BlockedItem, Long> {
}
