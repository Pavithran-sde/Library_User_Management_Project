package com.LibraryManagement.LibraryUserManagement.User.Repository;

import com.LibraryManagement.LibraryUserManagement.User.Entities.UserDevices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDeviceRepository extends JpaRepository<UserDevices, Long> {

    List<UserDevices> findAllByUserId(long id);
}
