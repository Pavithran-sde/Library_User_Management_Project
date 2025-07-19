package com.LibraryManagement.LibraryUserManagement.User.Services;

import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDevice.UserDeviceRequestDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDevice.UserDeviceResponseDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto.UserDeviceDeleteRequestDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Entities.UserDevices;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Mappers.UserDeviceMapper;
import com.LibraryManagement.LibraryUserManagement.User.Mappers.UserMapper;
import com.LibraryManagement.LibraryUserManagement.User.Repository.UserDeviceRepository;
import com.LibraryManagement.LibraryUserManagement.User.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserDevicesService {

    @Autowired
    private UserDeviceRepository userDeviceRepository;

    @Autowired
    private UserDeviceMapper userDeviceMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    public List<UserDeviceResponseDto> getAllDevices() throws Exception{
        List<UserDevices> userDevicesList = userDeviceRepository.findAll();
        if(userDevicesList.isEmpty()) throw new NoContentFoundException("No UserDevices Exist");
        return getAllDeviceDetails(userDevicesList);
    }

    private List<UserDeviceResponseDto> getAllDeviceDetails(List<UserDevices> userDevicesList) {
            return userDevicesList.stream().map(userDevice ->{
                UserDeviceResponseDto userDeviceResponseDto = userDeviceMapper.toDto(userDevice);
                userDeviceResponseDto.setUserInfo(userMapper.toBooking_UserInfoDto(userDevice.getUser()));
                return userDeviceResponseDto;
            }).toList();
    }


    public UserDeviceResponseDto getDeviceById(long id) throws Exception {
        UserDevices userDevice = userDeviceRepository.findById(id).orElseThrow(() -> new NotFoundException("No Such Device Found"));
        UserDeviceResponseDto dto = userDeviceMapper.toDto(userDevice);
        dto.setUserInfo(userMapper.toBooking_UserInfoDto(userDevice.getUser()));
        return dto;
    }


    public List<UserDeviceResponseDto> getDeviceByUserId(long id) throws Exception{
        List<UserDevices> userDevicesList = userDeviceRepository.findAllByUserId(id);
        if(userDevicesList.isEmpty()) throw new NotFoundException("No Devices associated with the User");
        return getAllDeviceDetails(userDevicesList);
    }


    public UserDeviceResponseDto addDevice(@Valid UserDeviceRequestDto dto) throws Exception {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException("No Such User Exists"));
        UserDevices userDevice = userDeviceMapper.fromDto(dto);
        userDevice.setUser(user);
        userDevice.setRegisteredTime(LocalDateTime.now());
        userDeviceRepository.save(userDevice);
        UserDeviceResponseDto responseDto = userDeviceMapper.toDto(userDevice);
        responseDto.setUserInfo(userMapper.toBooking_UserInfoDto(user));
        return responseDto;
    }

    public UserDeviceResponseDto updateDevice(long id ,@Valid UserDeviceRequestDto dto) throws Exception {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException("No Such User Exist"));
        UserDevices userDevices = userDeviceRepository.findById(id).orElseThrow(() -> new NotFoundException("No Such Device Exist"));

        if(userDevices.getUser().getId() == dto.getUserId()){
            userDevices.setDeviceTypeEnum(dto.getDeviceTypeEnum());
            userDevices.setColour(dto.getColour());
            userDevices.setManufacturer(dto.getManufacturer());
            userDevices.setModelName(dto.getModelName());
            userDeviceRepository.save(userDevices);

            UserDeviceResponseDto userDeviceResponseDto = userDeviceMapper.toDto(userDevices);
            userDeviceResponseDto.setUserInfo(userMapper.toBooking_UserInfoDto(user));
            return userDeviceResponseDto;
        }
        else{
            throw new IllegalAccessException("Invalid or Wrong request");
        }

    }


    public void deleteDeviceById(UserDeviceDeleteRequestDto dto) throws Exception {
        UserDeviceResponseDto responseDto = getDeviceById(dto.getDeviceId());
        if(responseDto.getUserInfo().getId() == dto.getUserId()){
            userDeviceRepository.deleteById(dto.getDeviceId());
        }
        else{
            throw new IllegalAccessException("Invalid or Wrong request");
        }
    }


}
