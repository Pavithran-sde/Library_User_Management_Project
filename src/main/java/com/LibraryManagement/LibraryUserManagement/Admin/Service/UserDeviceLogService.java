package com.LibraryManagement.LibraryUserManagement.Admin.Service;


import com.LibraryManagement.LibraryUserManagement.Admin.Entities.UserDeviceLog;
import com.LibraryManagement.LibraryUserManagement.Admin.Mappers.UserDeviceLogMapper;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.UserDeviceLogRepository;
import com.LibraryManagement.LibraryUserManagement.User.Entities.UserDevices;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Mappers.UserDeviceMapper;
import com.LibraryManagement.LibraryUserManagement.User.Mappers.UserMapper;
import com.LibraryManagement.LibraryUserManagement.User.Repository.UserDeviceRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.UserDeviceLog.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserDeviceLogService {

    @Autowired
    private UserDeviceLogRepository userDeviceLogRepository;

    @Autowired
    private UserDeviceLogMapper userDeviceLogMapper;

    @Autowired
    private UserDeviceMapper userDeviceMapper;

    @Autowired
    private UserMapper userMapper;

//    @Autowired
//    private UserRepository userRepository;


    @Autowired
    private UserDeviceRepository userDeviceRepository;

    @Autowired
    private LibraryEntryService libraryEntryService;



    public List<UserDeviceLogResponseDto> getAllDeviceLogs() throws Exception {
        List<UserDeviceLog> userDeviceLogList = userDeviceLogRepository.findAll();
        if(userDeviceLogList.isEmpty()) throw new NoContentFoundException("NO Device Logs found");
        return getAllLogDetails(userDeviceLogList);
    }

    private List<UserDeviceLogResponseDto> getAllLogDetails(List<UserDeviceLog> userDeviceLogList) {

        return userDeviceLogList.stream().map(logs ->{
            UserDeviceLogResponseDto responseDto = userDeviceLogMapper.toDto(logs);
            responseDto.setUserDeviceDetails(userDeviceMapper.toDto(logs.getUserDevices()));
            responseDto.getUserDeviceDetails().setUserInfo(userMapper.toBooking_UserInfoDto(logs.getUserDevices().getUser()));
            return responseDto;
        }).toList();
    }


    public UserDeviceLogResponseDto getAllDeviceLogsById(long id) throws Exception{
        UserDeviceLog userDeviceLog = userDeviceLogRepository.findById(id).orElseThrow(() -> new NotFoundException("No such log entry exists"));
        UserDeviceLogResponseDto responseDto = userDeviceLogMapper.toDto(userDeviceLog);
        responseDto.setUserDeviceDetails(userDeviceMapper.toDto(userDeviceLog.getUserDevices()));
        return responseDto;
    }


    public List<UserDeviceLogResponseDto> getAllDeviceLogsByDeviceId(long id) throws Exception{
        List<UserDeviceLog> userDeviceLogList = userDeviceLogRepository.findByUserDevicesId(id);
        if(userDeviceLogList.isEmpty()) throw new NoContentFoundException("No such Device exist");
        return getAllLogDetails(userDeviceLogList);
    }

    @Transactional
    public UserDeviceLogResponseDto addEntry(UserDeviceLogRequestDto requestDto) throws Exception {

        UserDevices userDevice = userDeviceRepository.findById(requestDto.getDeviceId()).orElseThrow(() -> new NotFoundException("No such device Exist"));
        if(userDevice.getUser().getId() == requestDto.getUserId()){
            UserDeviceLog userDeviceLog = new UserDeviceLog();
            userDeviceLog.setUserDevices(userDevice);
            userDeviceLog.setLogInTime(LocalDateTime.now());
            userDeviceLog.setLibraryEntryLog(libraryEntryService.getLibraryEntryLogById(requestDto.getLibraryEntryNo()));
            userDeviceLogRepository.save(userDeviceLog);
            
            UserDeviceLogResponseDto responseDto = userDeviceLogMapper.toDto(userDeviceLog);
            responseDto.setUserDeviceDetails(userDeviceMapper.toDto(userDevice));
            responseDto.getUserDeviceDetails().setUserInfo(userMapper.toBooking_UserInfoDto(userDevice.getUser()));
            //update the Library Entry log to set the device status
            libraryEntryService.setDeviceEnumInLibraryEntryService(requestDto.getLibraryEntryNo());
            return responseDto;
        }else{
            throw new IllegalAccessException("Invalid or Wrong Request");
        }
    }

    @Transactional
    public UserDeviceLogResponseDto clearEntry(@Valid UserDeviceLogRequestDto requestDto) throws Exception{
        UserDevices userDevice = userDeviceRepository.findById(requestDto.getDeviceId()).orElseThrow(() -> new NotFoundException("No such device Exist"));
        if(userDevice.getUser().getId() == requestDto.getUserId()){
            UserDeviceLog existingUserDeviceLog = userDeviceLogRepository.findByUserDevicesIdAndLogOutTime(requestDto.getDeviceId(), null);
            existingUserDeviceLog.setLogOutTime(LocalDateTime.now());
            userDeviceLogRepository.save(existingUserDeviceLog);

            UserDeviceLogResponseDto responseDto = userDeviceLogMapper.toDto(existingUserDeviceLog);
            responseDto.setUserDeviceDetails(userDeviceMapper.toDto(userDevice));
            responseDto.getUserDeviceDetails().setUserInfo(userMapper.toBooking_UserInfoDto(userDevice.getUser()));
            return responseDto;
        }else{
            throw new IllegalAccessException("Invalid or Wrong Request");
        }
    }

    public void deleteLogEntryById(DeleteUserDeviceLogEntryDto deleteDto) throws Exception {
        getAllDeviceLogsById(deleteDto.getLogId());
        userDeviceLogRepository.deleteById(deleteDto.getLogId());

    }
}
