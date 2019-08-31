package com.github.knoweth.server.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.teavm.flavour.json.JsonPersistable;

import javax.persistence.*;
import java.util.*;

/**
 * Represents a user and associated metadata in the Knoweth system.
 */
@Entity
@JsonPersistable
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;
    // Usernames must be unique
    @Column(unique = true)
    private String username;
    // Emails must also be unique
    @Column(unique = true)
    private String email;
    private String password;
    @Transient
    @JsonIgnore
    private Set<String> roles;

    /**
     * Parameterless constructor for Hibernate use only. End users should not
     * use this.
     */
    public User() {
        roles = new HashSet<>();
        roles.add("USER");
    }

    /**
     * Create a new User.
     * @param username the username of the new user
     * @param email the email of the user
     * @param password the plaintext password of the user.
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Below functions are for Spring authentication purposes.

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
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
