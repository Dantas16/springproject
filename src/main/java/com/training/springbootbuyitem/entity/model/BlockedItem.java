package com.training.springbootbuyitem.entity.model;

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
}
