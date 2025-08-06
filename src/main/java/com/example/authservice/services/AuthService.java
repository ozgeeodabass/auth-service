package com.example.authservice.services;

import com.example.authservice.models.User;
import com.example.authservice.models.requests.LoginRequest;
import com.example.authservice.models.requests.RegisterRequest;
import com.example.authservice.models.responses.JwtResponse;
import com.example.authservice.repositories.UserRepository;
import com.example.authservice.security.jwt.JwtUtils;
import com.example.authservice.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.username()))
            return null;
        User user = createUserFromRegisterRequest(registerRequest);
        userRepository.save(user);
        return user;
    }

    private User createUserFromRegisterRequest(RegisterRequest registerRequest) {
        return User.builder().username(registerRequest.username())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .gender(!registerRequest.gender().isEmpty()?registerRequest.gender():null)
                .name(!registerRequest.name().isEmpty()?registerRequest.name():null)
                .phoneNumber(!registerRequest.phoneNumber().isEmpty()?registerRequest.phoneNumber():null)
                .role(registerRequest.role())
                .build();
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        //first, check if the user exists and is not marked as deleted
        Optional<User> user = userRepository.findByUsernameAndDeletedIsFalse(loginRequest.username());
        if(user.isEmpty())
            throw new UsernameNotFoundException("USER_NOT_FOUND");

        try{
            //try to authenticate the user using username and password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

            //set the authenticated user in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //generate JWT token
            String jwt = jwtUtils.generateJwtToken(authentication);

            //get authenticated user details
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            //return token and user details
            return new JwtResponse(jwt,
                    "Bearer",
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    userDetails.getGender(),
                    userDetails.getName(),
                    userDetails.getPhoneNumber(),
                    userDetails.getAuthorities().get(0));
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("WRONG_PASSWORD");
        }
    }
}
