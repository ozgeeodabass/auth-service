package com.example.authservice.controllers;

import com.example.authservice.models.User;
import com.example.authservice.models.requests.LoginRequest;
import com.example.authservice.models.requests.RegisterRequest;
import com.example.authservice.models.responses.JwtResponse;
import com.example.authservice.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User registeredUser = authService.registerUser(registerRequest);
            if (registeredUser != null) {
                return ResponseEntity.ok(registeredUser);
            } else {
                return new ResponseEntity<>("THIS_USER_ALREADY_REGISTERED", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //This endpoint receives a LoginRequest, authenticates the user, and returns a JWT token inside response.
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse response = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ///e.printStackTrace();
            if (e.getClass().equals(AccessDeniedException.class)) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
            }else if(e.getClass().equals(UsernameNotFoundException.class)){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
            else if(e.getClass().equals(BadCredentialsException.class)){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
