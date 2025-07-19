package com.LibraryManagement.LibraryUserManagement.Admin.Service;

import com.LibraryManagement.LibraryUserManagement.Admin.DTO.EntryLogDtos.EntryResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.EntryLogDtos.LibraryEntryResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.EntryLogDtos.SecurityLibraryExitDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.UserDeviceLog.UserDeviceLogResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.LibraryEntryLog;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.UserDeviceLog;
import com.LibraryManagement.LibraryUserManagement.Admin.Enums.LibraryLogDeviceEnum;
import com.LibraryManagement.LibraryUserManagement.Admin.Mappers.LibraryEntryLogMapper;
import com.LibraryManagement.LibraryUserManagement.Admin.Mappers.UserDeviceLogMapper;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.LibraryEntryLogRepository;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.UserDeviceLogRepository;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Mappers.UserDeviceMapper;
import com.LibraryManagement.LibraryUserManagement.User.Mappers.UserMapper;
import com.LibraryManagement.LibraryUserManagement.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LibraryEntryService {

    @Autowired
    private LibraryEntryLogRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    UserDeviceLogRepository userDeviceLogRepository;

    @Autowired
    private UserDeviceMapper userDeviceMapper;

    @Autowired
    private LibraryEntryLogMapper libraryEntryLogMapper;

    @Autowired
    private UserDeviceLogMapper userDeviceLogMapper;

    @Transactional
    public EntryResponseDto makeLibraryEntry(long userId) throws Exception{
        //check if the user has already made an entry on that particular day
        LibraryEntryLog existingEntry = repository.findByUserIdAndLoggedOutTime(userId, null);
        User user;
            //if entry does not exist then
        if(existingEntry == null){
             user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("No such user found - home controller"));
            if(user != null){
                LibraryEntryLog entryLog = new LibraryEntryLog(
                        user, LocalDateTime.now(), null, LibraryLogDeviceEnum.HAS_NO_DEVICE
                );
                LibraryEntryLog savedEntry = repository.save(entryLog);
                //make changes here to dynamically allocate whether they have brought any device with them
                return new EntryResponseDto(userMapper.toBooking_UserInfoDto(user), "logged In", savedEntry.getId());
            }
            else {
                throw new NotFoundException("No such user exist to make an entry");
                }
        } else{
//            //make an out entry from library
//            user = existingEntry.getUser();
//            existingEntry.setLoggedOutTime(LocalDateTime.now());
//            LibraryEntryLog savedEntry = repository.save(existingEntry);
//            // also add logic to clear all booking and entries within the library
//            return new EntryResponseDto(userMapper.toBooking_UserInfoDto(user), "logged out", savedEntry.getId());

            throw new Exception("You are already Logged Into library");
        }
    }

    public LibraryEntryLog getLibraryEntryLogById(long id) throws Exception{
        return repository.findById(id).orElseThrow(() -> new NotFoundException("No such Library Entry found"));
    }


    @Transactional
    public void setDeviceEnumInLibraryEntryService(long entryId) throws Exception{
        LibraryEntryLog existingEntry = repository.findById(entryId).orElseThrow(() -> new NotFoundException("No such Library Entry found"));
        existingEntry.setDeviceEnum(LibraryLogDeviceEnum.HAS_BROUGHT_DEVICE);
        repository.save(existingEntry);
    }

    public SecurityLibraryExitDto getDetailsOfEntry(long entryNumber) throws Exception {

        SecurityLibraryExitDto dto = new SecurityLibraryExitDto();
        LibraryEntryLog libraryEntryLog = repository.findById(entryNumber).orElseThrow(() -> new NotFoundException("No such Library entry found"));
        List<UserDeviceLog> userDeviceLog = userDeviceLogRepository.findByLibraryEntryId(entryNumber);

        dto.setLibraryEntryId(libraryEntryLog.getId());
        dto.setUser(userMapper.toBooking_UserInfoDto(libraryEntryLog.getUser()));
        dto.setLibraryExitDeviceInfoDtoList(
                libraryEntryLog.getUserDeviceLogList().stream().map(
                        userdeviceLog -> {
                             return userDeviceMapper.toExitDto(userdeviceLog.getUserDevices());
                }).toList());

        dto.setNoOfDevices(libraryEntryLog.getUserDeviceLogList().size());
        return dto;
    }


    //general service methods below

    public List<LibraryEntryResponseDto> getAllLibraryEntryLogs() throws Exception{
       List<LibraryEntryLog> logList = repository.findAll();

       if(logList.isEmpty())
          throw new NoContentFoundException("No Logs Exists");

       return getAllLibraryEntryLogsDetails(logList);
    }

    private List<LibraryEntryResponseDto> getAllLibraryEntryLogsDetails(List<LibraryEntryLog> logList) {
        return logList.stream().map(log -> {
               LibraryEntryResponseDto entry = libraryEntryLogMapper.toDto(log);
               entry.setUserDevices(log.getUserDeviceLogList().stream().map(userDeviceLog -> {
                   UserDeviceLogResponseDto dto =  userDeviceLogMapper.toDto(userDeviceLog);
                    dto.setUserDeviceDetails(userDeviceMapper.toDto(userDeviceLog.getUserDevices()));
                    return dto;
               }).toList());
               return entry;
        }).toList();
    }


}

