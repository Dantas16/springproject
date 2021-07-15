package com.training.springbootbuyitem.entity.model;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userUid")
    @JoinColumn(name = "user_id")
    private Buyer buyer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("itemUid")
    @JoinColumn(name = "item_id")
    private Item item;

    private Date date;
    private BigInteger quantity;
    private BigDecimal price;
    private String state;

}
