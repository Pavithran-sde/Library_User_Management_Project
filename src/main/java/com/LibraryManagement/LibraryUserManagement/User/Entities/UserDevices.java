package com.LibraryManagement.LibraryUserManagement.User.Entities;


import com.LibraryManagement.LibraryUserManagement.Admin.Entities.UserDeviceLog;
import com.LibraryManagement.LibraryUserManagement.User.Enum.DeviceLogEnum;
import com.LibraryManagement.LibraryUserManagement.User.Enum.DeviceTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDevices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceTypeEnum deviceTypeEnum;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    private String colour;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceLogEnum isActive;

    @Column(nullable = false)
    private LocalDateTime registeredTime;

    @JsonIgnore
    @OneToMany(mappedBy = "userDevices")
    List<UserDeviceLog> userDeviceLogList;
}
