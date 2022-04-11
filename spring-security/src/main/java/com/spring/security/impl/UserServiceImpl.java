package com.spring.security.impl;

import com.spring.security.model.User;
import com.spring.security.repo.UserRepository;
import com.spring.security.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {
    @Autowired
    private UserRepository repo;

    @Autowired
    private BCryptPasswordEncoder pwdEncoder;

    @Override
    public Integer saveUser(User user) {
        //ENCODE PASSWORD
        user.setPassword(
                pwdEncoder.encode(
                        user.getPassword())
        );
        return repo.save(user).getId();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }


    /*-------------------------DEFAULT METHOD FROM SECURITY---------------------------*/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = findByUsername(username);
        if(user.isEmpty())
            throw new UsernameNotFoundException("User not exist");
        //read from db
        User user1 = user.get();
        System.out.println("USER SERVICE IMPl 49 "+ user1.getPassword()+" "+user1.getUsername());
        return new org.springframework.security.core.userdetails.User(
                username,
                user1.getPassword(),
                user1.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role))
                        .collect(Collectors.toList())
        );
    }
}
