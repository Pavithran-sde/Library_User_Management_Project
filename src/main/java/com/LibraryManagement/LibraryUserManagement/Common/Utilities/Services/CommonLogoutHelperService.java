package com.LibraryManagement.LibraryUserManagement.Common.Utilities.Services;

import com.LibraryManagement.LibraryUserManagement.Admin.DTO.EntryLogDtos.LibraryExitDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.QR.LibraryEntryQRScannerDto;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.FloorEntryLog;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.LibraryEntryLog;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.UserDeviceLog;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.FloorEntryLogRepository;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.LibraryEntryLogRepository;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.UserDeviceLogRepository;
import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortBookingDto.ClearChargingPortBookingDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.TableBookingDto.ClearTableBookingDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Mappers.UserMapper;
import com.LibraryManagement.LibraryUserManagement.User.Services.ChargingPortBookingService;
import com.LibraryManagement.LibraryUserManagement.User.Services.TableBookingService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class CommonLogoutHelperService {

    @Autowired
    LibraryEntryLogRepository libraryEntryLogRepository;

    @Autowired
    UserDeviceLogRepository userDeviceLogRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SecurityUtilService securityUtilService;

    @Autowired
    private TableBookingService tableBookingService;

    @Autowired
    private ChargingPortBookingService chargingPortBookingService;

    @Autowired
    private FloorEntryLogRepository floorEntryLogRepository;

    private final static  String SECRET_KEY_PREFIX = "daily_qr_secret_key:";

    private final static String SECRET = "f3c2b1a8-7f16-47d3-bf4d-28e9e9f3c1ab.";

    public LibraryEntryQRScannerDto validateLibraryEntryToken(String token) {
        LibraryEntryQRScannerDto dto = new LibraryEntryQRScannerDto();

        dto.setValid(validateAdminLibraryQr(token));
        return dto;

    }

    public LibraryExitDto validateAdminExitQR(String token){
        LibraryExitDto dto = new LibraryExitDto();
        dto.setValid(validateAdminLibraryQr(token));
        return dto;
    }

    private boolean validateAdminLibraryQr(String token){
        try {
            DecodedJWT decoded = JWT.decode(token);
            String date = decoded.getClaim("date").asString();
            String purpose = decoded.getClaim("purpose").asString();
            String validFrom = decoded.getClaim("valid_from").asString();
            String validUntil = decoded.getClaim("valid_until").asString();

            if (!"library_entry".equals(purpose)) {
                return false;
            }

            LocalTime now = LocalTime.now();
            LocalTime start = LocalTime.parse(validFrom);
            LocalTime end = LocalTime.parse(validUntil);

            if (now.isBefore(start) || now.isAfter(end)) {
                return false;
            }

            String secret = redisTemplate.opsForValue().get(SECRET_KEY_PREFIX + date);
            if (secret == null) {
                return false;
            }

            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token); // will throw if invalid
            return true;
        } catch (Exception e) {
            return false;
        }
    }



    public LibraryExitDto validateUserExitQR(String token){
        LibraryExitDto dto = new LibraryExitDto();
        try {
            DecodedJWT decoded = JWT.decode(token);
            String libraryEntryId = decoded.getClaim("libraryEntryId").asString();
            String purpose = decoded.getClaim("purpose").asString();
            String validFrom = decoded.getClaim("validFrom").asString();
            String validUntil = decoded.getClaim("validTill").asString();
            long entryId = Long.parseLong(libraryEntryId);

            if(!libraryEntryLogRepository.existsById(entryId)) {
                dto.setValid(false);
                return dto;
            }

            if (!"library_exit".equals(purpose)) {
                dto.setValid(false);
                return dto;
            }

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime start = LocalDateTime.parse(validFrom);
            LocalDateTime end = LocalDateTime.parse(validUntil);

            if (now.isBefore(start) || now.isAfter(end)) {
                dto.setValid(false);
                return dto;
            }

            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token); // will throw if invalid

            LibraryEntryLog libraryEntryLog = libraryEntryLogRepository.findById(entryId).orElseThrow(() -> new NotFoundException("No such Library entry " + entryId + " exist"));

            dto.setValid(true);
            dto.setActivity("logout");
            dto.setUser(userMapper.toBooking_UserInfoDto(libraryEntryLog.getUser()));
            dto.setEntryNumber(entryId);
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            dto.setValid(false);
            return dto;
        }
    }
    @Transactional
    public void logoutUser(long entryNumber) throws Exception{

        LocalDateTime currentDataAndTime = LocalDateTime.now();
        LibraryEntryLog libraryEntryLog = libraryEntryLogRepository.findById(entryNumber).orElseThrow(() -> new NotFoundException("No such library Entry "+ entryNumber + " exist"));
        libraryEntryLog.setLoggedOutTime(currentDataAndTime);
        libraryEntryLogRepository.save(libraryEntryLog);

        List<UserDeviceLog> userDeviceLogList = userDeviceLogRepository.findByLibraryEntryId(entryNumber);

        if(!userDeviceLogList.isEmpty()){

            userDeviceLogList = userDeviceLogList.stream().map(userDeviceLog -> {
                userDeviceLog.setLogOutTime(currentDataAndTime);
                return userDeviceLog;
            }).toList();
            userDeviceLogRepository.saveAll(userDeviceLogList);
        }
        //get user
        User user = libraryEntryLog.getUser();
        clearBookingsOfUser(user);
        FloorEntryLog floorEntryLog = floorEntryLogRepository.findByUserIdAndLoggedOutTime(user.getId(), null);
        if(floorEntryLog != null){
            floorEntryLog.setLoggedOutTime(currentDataAndTime);
            floorEntryLogRepository.save(floorEntryLog);
        }

    }

    @Transactional
    public void clearBookingsOfUser(User user){
        try{
            tableBookingService.clearTableBooking(new ClearTableBookingDto(user.getId()));
            chargingPortBookingService.clearChargingPortBooking(new ClearChargingPortBookingDto(user.getId()));

        } catch (Exception e){
            //do nothing
        }
    }

    public long getLibraryEntryId(String token){
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
        DecodedJWT decoded = JWT.decode(token);
        String libraryEntryId = decoded.getClaim("libraryEntryId").asString();
        return Long.parseLong(libraryEntryId);
    }

}
