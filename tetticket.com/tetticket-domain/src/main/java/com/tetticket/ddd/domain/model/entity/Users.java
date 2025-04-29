package com.tetticket.ddd.domain.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@Entity
@Table(name = "users")
public class Users implements UserDetails {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long user_id;

 @NotBlank(message = "Username is mandatory")
 private String username;

 @NotBlank(message = "Password is mandatory")
 @Size(min = 8, message = "Password must be at least 8 characters long")
 private String password;

 @NotBlank(message = "Email is mandatory")
 @Email(message = "Email should be valid")
 private String email;

 @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
 @JoinTable(name="user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name="role_id"))
 private Set<Roles> roles = new HashSet<>();

    // Config for UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Roles role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole_name()));
        }
        return authorities;
    }
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
