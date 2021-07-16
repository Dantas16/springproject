package com.training.springbootbuyitem.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Proxy(lazy = false)
@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user", unique = true, nullable = false)
    private Long userUid;
    @Column(unique = true)
    private String name;
    private String role;
    private String username;
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<Purchase> purchases = new HashSet<>();

    public void setPurchase(Purchase purchase) {
        purchases.add(purchase);
    }

}
