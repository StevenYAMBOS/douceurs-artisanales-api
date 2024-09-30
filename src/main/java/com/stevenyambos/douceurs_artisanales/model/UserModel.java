package com.stevenyambos.douceurs_artisanales.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Data
@AllArgsConstructor
@Document(collection = "user")
public class UserModel implements UserDetails {

    public enum Role {
        USER, OWNER, ADMIN;
    }

    private Role role = Role.USER;

    @Id
    private String id;

    @NotBlank(message = "Le prénom est obligatoire.")
    private String firstname;

    @NotBlank(message = "Le nom de famille est obligatoire.")
    private String lastname;

    @NotBlank(message = "Le mot de passe est obligatoire.")
    private String password;

    @NotBlank(message = "L'adresse Email est obligatoire.")
    @Email(message = "Le format de l'adresse Email n'est pas valide.")
    private String email;

//    @NotBlank(message = "L'image de profile est obligatoire.")
    private String profilePicture = "";

    private String[] Comments = {};

    private Boolean isOwner = false;

    private Boolean isSuperuser = false;

    private Date createdAt;

    private Date updatedAt;

    private Date lastLogin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }
    @Override
    public String getUsername() {
        return "";
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

}
