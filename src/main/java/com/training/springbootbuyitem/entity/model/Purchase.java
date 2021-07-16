package com.training.springbootbuyitem.entity.model;

import com.training.springbootbuyitem.enums.EnumPurchaseState;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_item")
    private Item item;

    private Date date;
    private BigInteger quantity;
    private BigInteger quantityDispatched;
    private BigDecimal price;
    private String state;

    public Purchase(User user, Item item, Date date, BigInteger quantity, BigInteger quantityDispatched, BigDecimal price) {
        this.user = user;
        this.item = item;
        this.date = date;
        this.quantity = quantity;
        this.quantityDispatched = quantityDispatched;
        this.price = price;
        this.state = EnumPurchaseState.PENDING.name();
    }
}
