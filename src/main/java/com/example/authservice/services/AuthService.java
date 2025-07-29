package com.example.authservice.services;

import com.example.authservice.models.User;
import com.example.authservice.models.requests.RegisterRequest;
import com.example.authservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername()))
            return null;
        User user = createUserFromRegisterRequest(registerRequest);
        userRepository.save(user);
        return user;
    }

    private User createUserFromRegisterRequest(RegisterRequest registerRequest) {
        return User.builder().username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(encoder.encode(registerRequest.getPassword()))
                .gender(!registerRequest.getGender().isEmpty()?registerRequest.getGender():null)
                .name(!registerRequest.getName().isEmpty()?registerRequest.getName():null)
                .phoneNumber(!registerRequest.getPhoneNumber().isEmpty()?registerRequest.getPhoneNumber():null)
                .build();
    }
}
