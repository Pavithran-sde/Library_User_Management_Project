package com.LibraryManagement.LibraryUserManagement.User.Repository;

import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //find by email - unique
    Optional<User> findByEmail(String email);

    //find by phoneNo - unique
    Optional<User> findByPhoneNo(String phoneNo);




}
