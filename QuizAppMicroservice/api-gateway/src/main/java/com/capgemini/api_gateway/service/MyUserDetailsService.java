package com.capgemini.api_gateway.service;

import com.capgemini.api_gateway.entity.UserPrincipal;
import com.capgemini.api_gateway.entity.Users;
import com.capgemini.api_gateway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
        System.out.println(">>> MyUserDetailsService Bean Created");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(">>> Loading user by username: " + username);
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            System.out.println(">>> User Not Found: " + username);
            throw new UsernameNotFoundException("User Not Found");
        }
        System.out.println(">>> User Found: " + user);
        return new UserPrincipal(user);
    }
}

