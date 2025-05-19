package com.capgemini.security_service.config;

import com.capgemini.security_service.entity.MyUser;
import com.capgemini.security_service.repository.MyUserRepository;
import com.capgemini.security_service.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final MyUserRepository userRepository;

    public OAuth2AuthenticationSuccessHandler(JwtService jwtService, MyUserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        Map<String, Object> attributes = oauthUser.getAttributes();
        attributes.forEach((key, value) -> System.out.println(key + " : " + value));


        Optional<MyUser> existingUser = userRepository.findByUsername(email);
        if (existingUser.isEmpty()) {
            MyUser newUser = new MyUser();
            newUser.setUsername(email);
            newUser.setPassword(""); // No password for Google users
            userRepository.save(newUser);
        }

        String token = jwtService.generateToken(email);
        System.out.println(token+"--------------------------------------");
        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + token + "\"}");
    }
}
