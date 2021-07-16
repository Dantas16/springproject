package com.training.springbootbuyitem.repository;

import com.training.springbootbuyitem.entity.model.User;
import com.training.springbootbuyitem.entity.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationRepository extends JpaRepository<UserAuth, Long> {
    Optional<UserAuth> findByUsername(String username);
}
