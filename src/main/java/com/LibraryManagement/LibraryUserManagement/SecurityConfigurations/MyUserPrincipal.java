package com.LibraryManagement.LibraryUserManagement.SecurityConfigurations;

import com.LibraryManagement.LibraryUserManagement.User.Entities.UserCredentials;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MyUserPrincipal implements UserDetails {

    private long id;
    private String username;
    private String password;
    private boolean isEnabled;
    private boolean isAccountNonLocked;
    private String role;
    private boolean isAccountNonExpired;

    public MyUserPrincipal(UserCredentials userCredentials){
        this.id = userCredentials.getId();
        this.username = userCredentials.getUserName();
        this.password = userCredentials.getUserPassword();
        this.isEnabled = userCredentials.isEnabled();
        this.isAccountNonExpired = !userCredentials.isAccountExpired();
        this.role = userCredentials.getRole();
        this.isAccountNonLocked = !userCredentials.isBlocked();
        System.out.println(this);
    }


    public long getUserId(){
        return this.id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+this.role));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() { return this.isAccountNonExpired;}

    @Override
    public boolean isAccountNonLocked(){return this.isAccountNonLocked;}

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
