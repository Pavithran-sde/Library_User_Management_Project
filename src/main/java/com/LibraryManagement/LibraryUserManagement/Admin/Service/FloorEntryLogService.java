package com.LibraryManagement.LibraryUserManagement.Admin.Service;


import com.LibraryManagement.LibraryUserManagement.Admin.DTO.EntryLogDtos.EntryResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.QR.FloorEntryQRScannerDto;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Floor;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.FloorEntryLog;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.LibraryEntryLog;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.FloorEntryLogRepository;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.FloorRepository;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.LibraryEntryLogRepository;
import com.LibraryManagement.LibraryUserManagement.Common.Utilities.Services.CommonLogoutHelperService;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Mappers.UserMapper;
import com.LibraryManagement.LibraryUserManagement.User.Repository.UserRepository;
import com.LibraryManagement.LibraryUserManagement.Admin.Exception.AlreadyLoggedInFloor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class FloorEntryLogService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private FloorEntryLogRepository floorEntryLogRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LibraryEntryLogRepository libraryEntryLogRepository;

    @Autowired
    CommonLogoutHelperService commonLogoutHelperService;

    @Transactional
    public EntryResponseDto makeFloorEntry(long userId, FloorEntryQRScannerDto dto) throws Exception{
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("No such user found to make floor entry"));
        Floor floor = floorRepository.findByFloorNameAndWing(dto.getFloorName(), dto.getWing());
        if(floor == null)
            throw new NotFoundException("No such Floor found for floor entry");

        //check if the user has made a library entry
        LibraryEntryLog libraryEntryLog = libraryEntryLogRepository.findByUserIdAndLoggedOutTime(user.getId(), null);

        if(libraryEntryLog != null){
            FloorEntryLog existingEntry = floorEntryLogRepository.findByUserIdAndLoggedOutTime(userId, null);
            if(existingEntry == null){
                FloorEntryLog entryLog = new FloorEntryLog(user, floor, LocalDateTime.now(), null );
                FloorEntryLog savedEntry = floorEntryLogRepository.save(entryLog);
                return new EntryResponseDto(userMapper.toBooking_UserInfoDto(user), "loggedIn", savedEntry.getId());
            }
            else if (existingEntry.getFloor().getFloorName().equals(dto.getFloorName()) && (existingEntry.getFloor().getWing().equals(dto.getWing()))) {
                //logging out the user from the floor
                commonLogoutHelperService.clearBookingsOfUser(existingEntry.getUser());
                existingEntry.setLoggedOutTime(LocalDateTime.now());
                FloorEntryLog savedEntry = floorEntryLogRepository.save(existingEntry);
                return new EntryResponseDto(userMapper.toBooking_UserInfoDto(user), "loggedOut", savedEntry.getId());
            }
            else throw new AlreadyLoggedInFloor();
        } else
            throw new NotFoundException("You have to login into library to make a entry into floor");
        }
}
