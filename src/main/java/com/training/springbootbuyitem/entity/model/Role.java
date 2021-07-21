package com.training.springbootbuyitem.entity.model;

import lombok.*;
import org.hibernate.annotations.Proxy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Proxy(lazy = false)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue
    private long id;

    @Size(max = 100)
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

}
