package com.spring.security.service;

import com.spring.security.model.User;

import java.util.Optional;

public interface IUserService {
    Integer  saveUser(User user);
    Optional<User> findByUsername(String username);

}
