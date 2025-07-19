package com.LibraryManagement.LibraryUserManagement.SecurityConfigurations;

import com.LibraryManagement.LibraryUserManagement.User.Entities.UserCredentials;
import com.LibraryManagement.LibraryUserManagement.User.Repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserCredentialRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredentials userCredentials = repository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("No such User Found"));

        return new MyUserPrincipal(userCredentials);
    }
}
