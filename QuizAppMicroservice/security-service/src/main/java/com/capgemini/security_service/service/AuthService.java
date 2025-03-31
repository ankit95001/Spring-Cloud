package com.capgemini.security_service.service;

import com.capgemini.security_service.config.AuthConfig;
import com.capgemini.security_service.entity.MyUser;
import com.capgemini.security_service.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    MyUserRepository repository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    public String saveUser(MyUser credential) {
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        repository.save(credential);
        return "user added to the system";
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
