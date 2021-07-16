package com.training.springbootbuyitem.entity.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

@Proxy(lazy = false)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Item extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_item", unique = true, nullable = false)
	private Long itemUid;
	@Column(unique = true)
	private String name;
	private String state;
	private String description;
	private String market;
	private BigInteger stock;
	private BigDecimal priceTag;

//	@ManyToMany
//	@JoinTable(name = "blocked_access",
//			joinColumns = @JoinColumn(name = "itemUid"),
//			inverseJoinColumns = @JoinColumn(name = "userUid"))
//	@JsonManagedReference
//	private Set<Buyer> blockedBuyers = new HashSet<Buyer>();

	@OneToMany(mappedBy = "item")
	private Set<Purchase> purchases = new HashSet<>();


	public Item(String name){
		this.name = name;
	}

//	public void addBlockedBuyer(Buyer buyer) {
//		blockedBuyers.add(buyer);
//	}

}
