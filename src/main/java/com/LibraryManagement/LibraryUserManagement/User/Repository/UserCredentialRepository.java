package com.LibraryManagement.LibraryUserManagement.User.Repository;

import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Entities.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredentials, Long> {

        Optional<UserCredentials> findByUserName(String userName);
}
