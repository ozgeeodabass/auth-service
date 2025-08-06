package com.example.authservice.security.services;

import java.util.*;
import com.example.authservice.models.ERole;
import com.example.authservice.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;


//custom implementation of Spring Security's UserDetails interface
//it holds user information to be stored in the SecurityContext after authentication
@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private String gender;
    private String name;
    private String phoneNumber;
    private ERole authorities;

    public static UserDetailsImpl build(User user) {

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getGender(),
                user.getName(),
                user.getPhoneNumber(),
                user.getRole());
    }

    @Override
    public List<ERole> getAuthorities() {
        return List.of(authorities);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}