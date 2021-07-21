package com.training.springbootbuyitem.service;

import com.training.springbootbuyitem.entity.model.User;
import com.training.springbootbuyitem.enums.EnumEntity;
import com.training.springbootbuyitem.error.EntityNotFoundException;
import com.training.springbootbuyitem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class UserService implements IUserService, UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> list() {
        return userRepository.findAll();
    }

    @Override
    public User get(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(EnumEntity.BUYER.name(), id)) ;
    }

    @Override
    public List<User> get(List<Long> id) {
        return null;
    }

    @Override
    public List<User> update(List<User> id) {
        return null;
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(get(id));
    }

    @Override
    public User update(User entity) {
        User persistedUser = get(entity.getUserUid());
        if (StringUtils.hasText(entity.getName())) {
            persistedUser.setName(entity.getName());
        }
        userRepository.save(persistedUser);
        return persistedUser;
    }

    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userAuth = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not foundwith username: " + username));

        return userAuth;
    }
}
