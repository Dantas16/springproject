package com.training.springbootbuyitem.repository;

import com.training.springbootbuyitem.entity.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
//    @Query("SELECT p FROM purchase p where p.date = :title AND t.description = :description")
    @Query(value = "SELECT * FROM purchase p \n" +
            "where p.id_item = :itemId and state = 'PENDING'\n" +
            "order by p.date \n" +
            "limit 1", nativeQuery=true)
    Purchase findOldestToDispatch(@Param("itemId") Long itemId);
//    Purchase findFirstByItemOderByDateAsc(@Param("itemId") Long itemId);
}
