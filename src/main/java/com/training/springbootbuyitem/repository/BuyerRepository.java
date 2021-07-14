package com.training.springbootbuyitem.repository;

import com.training.springbootbuyitem.entity.model.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {
}
