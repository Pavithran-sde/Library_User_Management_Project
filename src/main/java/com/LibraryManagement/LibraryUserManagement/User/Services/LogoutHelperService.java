package com.LibraryManagement.LibraryUserManagement.User.Services;


import com.LibraryManagement.LibraryUserManagement.Admin.DTO.EntryLogDtos.LibraryExitDto;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.LibraryEntryLog;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.UserDeviceLog;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.LibraryEntryLogRepository;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.UserDeviceLogRepository;
import com.LibraryManagement.LibraryUserManagement.Common.Utilities.Services.CommonLogoutHelperService;
import com.LibraryManagement.LibraryUserManagement.SecurityConfigurations.MyUserPrincipal;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Mappers.UserMapper;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Service
public class LogoutHelperService {

    private final static String SECRET = "f3c2b1a8-7f16-47d3-bf4d-28e9e9f3c1ab.";

    @Autowired
    private LibraryEntryLogRepository libraryEntryLogRepository;

    @Autowired
    private UserDeviceLogRepository userDeviceLogRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommonLogoutHelperService commonLogoutHelperService;


    public boolean validateUserPrincipal(MyUserPrincipal principal) {

        if (principal.isEnabled() && principal.isAccountNonExpired() && principal.isAccountNonLocked())
            return true;
        else
            return false;
    }


    public Map<String, String> getMethodToLogout(long userId) throws Exception {

        LibraryEntryLog existingLibraryEntry = libraryEntryLogRepository
                .findByUserIdAndLoggedOutTime(userId, null);

        if (existingLibraryEntry != null) {
            List<UserDeviceLog> existingDeviceLogList = userDeviceLogRepository
                    .findByLibraryEntryId(existingLibraryEntry.getId());

            if (existingDeviceLogList.isEmpty())
                return Map.of("hasDevices", "false",
                        "content", "scanner",
                        "libraryEntryId", existingLibraryEntry.getId()+"");
            else
                return Map.of("hasDevices", "true",
                        "content", getToken(existingLibraryEntry.getId()),
                        "libraryEntryId", existingLibraryEntry.getId()+"");
        } else {
            throw new NotFoundException("No Active Library Entry exists for " + existingLibraryEntry.getUser().getName());
        }


    }

    private String getToken(long libraryEntryId) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);

        return JWT.create()
                .withIssuer("library-admin")
                .withClaim("purpose", "library_exit")
                .withClaim("libraryEntryId", libraryEntryId+"")
                .withClaim("validFrom", LocalDateTime.now().toString())
                .withClaim("validTill", LocalDateTime.now().plusMinutes(10).toString())
                .withIssuedAt(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
                .sign(algorithm);
    }


    public LibraryExitDto validateLibraryUserExitToken(String token) {
        return commonLogoutHelperService.validateAdminExitQR(token);
    }

    public LibraryExitDto logoutUser(long entryNumber) throws Exception {
        commonLogoutHelperService.logoutUser(entryNumber);
        return checkLogoutStatus(entryNumber);
    }

    public LibraryExitDto checkLogoutStatus(long libraryEntryId) throws Exception {

        LibraryEntryLog existingEntry = libraryEntryLogRepository.findById(libraryEntryId).orElseThrow(() -> new NotFoundException("No such Library entry found"));
            LibraryExitDto dto = new LibraryExitDto();

        if (existingEntry.getLoggedOutTime() != null) {
            dto.setValid(true);
            dto.setEntryNumber(existingEntry.getId());
            dto.setUser(userMapper.toBooking_UserInfoDto(existingEntry.getUser()));
            dto.setActivity("logged out");
            return dto;
        } else {
            dto.setValid(false);
            return dto;
        }
    }

    public long getLibraryEntryId(String token) {
        return commonLogoutHelperService.getLibraryEntryId(token);
    }

}