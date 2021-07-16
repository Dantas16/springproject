package com.training.springbootbuyitem.entity.model;

import com.training.springbootbuyitem.enums.EnumEntity;
import com.training.springbootbuyitem.error.NotEnoughStockException;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class BlockedItem implements Serializable {

    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @PrimaryKeyJoinColumn(name = "item_id", referencedColumnName = "itemUid")
    private Item item;

    @Column(name = "reservedQuantity")
    private BigInteger reservedQuantity;

    public BlockedItem(Long id, Item item) {
        this.id = id;
        this.item = item;
        this.reservedQuantity = BigInteger.valueOf(0);
    }

}
