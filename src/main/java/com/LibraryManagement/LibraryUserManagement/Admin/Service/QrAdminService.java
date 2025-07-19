package com.LibraryManagement.LibraryUserManagement.Admin.Service;

import com.LibraryManagement.LibraryUserManagement.Admin.DTO.EntryLogDtos.LibraryExitDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.QR.FloorEntryQRScannerDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.QR.LibraryEntryQRScannerDto;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Floor;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.LibraryEntryQrCodes;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.FloorRepository;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.LibraryEntryLogRepository;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.LibraryEntryQrTokenLogRepository;
import com.LibraryManagement.LibraryUserManagement.Common.Utilities.Services.QRGenerationService;
import com.LibraryManagement.LibraryUserManagement.Common.Utilities.Services.CommonLogoutHelperService;
import com.LibraryManagement.LibraryUserManagement.SecurityConfigurations.MyUserPrincipal;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Services.LogoutHelperService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.zxing.WriterException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.time.*;

@Service
@AllArgsConstructor
public class QrAdminService {

    @Autowired
    private final RedisTemplate<String, String> redisTemplate;
    @Autowired
    private final LibraryEntryQrTokenLogRepository secretRepository;
    @Autowired
    private final QRGenerationService qrGenerationService;
    @Autowired
    private final FloorRepository floorRepository;
    @Autowired
    private LibraryEntryLogRepository libraryEntryLogRepository;
    @Autowired
    private LogoutHelperService logoutHelperService;
    @Autowired
    private CommonLogoutHelperService commonLogoutHelperService;

    private static final String SECRET_KEY_PREFIX = "daily_qr_secret_key:";

    public byte[] generateQrImageForToday() throws WriterException, IOException {
        String token = generateDailyToken();
        return qrGenerationService.generateQRCodeImage(token);
    }


    private String generateDailyToken() {

        String date = LocalDate.now().toString();
        String redisKey = SECRET_KEY_PREFIX + date;

        LibraryEntryQrCodes libraryEntryQrCodes = secretRepository.findByDate(LocalDate.now());
        if (libraryEntryQrCodes != null) {
            String token = libraryEntryQrCodes.getToken();

            if (redisTemplate.opsForValue().get(redisKey) == null) {
                redisTemplate.opsForValue().set(redisKey, libraryEntryQrCodes.getSecret());
            }

            return token;
        }

        // if not present in the database create a new secret and a token

        String newSecret = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(redisKey, newSecret, Duration.ofDays(1));

        Algorithm algorithm = Algorithm.HMAC256(newSecret);
        //creating token
        String newToken = JWT.create()
                .withIssuer("library-admin")
                .withIssuedAt(new Date())
                .withExpiresAt(getEndOfDay())
                .withClaim("date", date)
                .withClaim("purpose", "library_entry")
                .withClaim("valid_from", "07:30")
                .withClaim("valid_until", "21:00")
                .sign(algorithm);

        //saving the token and the secret to the database
        LibraryEntryQrCodes newRecord = new LibraryEntryQrCodes();
        newRecord.setDate(LocalDate.now());
        newRecord.setSecret(newSecret);
        newRecord.setToken(newToken);
        newRecord.setGeneratedAt(LocalDateTime.now());
        newRecord.setGeneratedAt(LocalDateTime.now());
        newRecord.setExpiresAt(LocalDate.now().atTime(21, 0));
        secretRepository.save(newRecord);

        return newToken;
    }


    private Date getEndOfDay() {
        LocalDateTime endOfDay = LocalDate.now().atTime(21, 0); // 9 PM
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    // validate Library Entry Token(...)
    public LibraryEntryQRScannerDto validateLibraryEntryToken(String token) throws Exception {
       return commonLogoutHelperService.validateLibraryEntryToken(token);
    }

    public byte[] getFloorQR(String floorName, String wing) throws Exception {
        Floor floor = floorRepository.findByFloorNameAndWing(floorName, wing);
        if (floor != null) {
            return qrGenerationService.generateQRCodeImage(floor.getToken());
        }
        throw new NotFoundException("No Such Floor Exist");
    }

    public FloorEntryQRScannerDto validateFloorEntryToken(String token, MyUserPrincipal principal) {
        FloorEntryQRScannerDto dto = new FloorEntryQRScannerDto();
        if (principal.isAccountNonExpired() && principal.isAccountNonLocked() && principal.isEnabled()) {
            try {
                DecodedJWT decoded = JWT.decode(token);
                String purpose = decoded.getClaim("purpose").asString();
                String floorName = decoded.getClaim("floorName").asString();
                String wing = decoded.getClaim("wing").asString();
                String openTime = decoded.getClaim("openTime").asString();
                String closeTime = decoded.getClaim("closeTime").asString();
                boolean active = decoded.getClaim("active").asBoolean();
                String rolesAllowed = decoded.getClaim("rolesAllowed").asString();

                if (!"floor_entry".equals(purpose) && active) {
                    dto.setValid(false);
                    return dto;
                }

                if (rolesAllowed.equals("GUEST") || rolesAllowed.equals(principal.getAuthorities().toString())) {
                    LocalTime now = LocalTime.now();
                    LocalTime start = LocalTime.parse(openTime);
                    LocalTime end = LocalTime.parse(closeTime);
                    if (now.isBefore(start) || now.isAfter(end)) {
                        dto.setValid(false);
                        return dto;
                    }

                    Algorithm algorithm = Algorithm.HMAC256(FloorService.SECRET);
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    verifier.verify(token); // will throw if invalid
                    dto.setValid(true);
                    dto.setFloorName(floorName);
                    dto.setWing(wing);
                    return dto;
                } else {
                    dto.setValid(false);
                    return dto;
                }
            } catch (Exception e) {
                dto.setValid(false);
                return dto;
            }
        } else{
            dto.setValid(false);
            return dto;
        }

    }

    public LibraryExitDto validateLibraryUserExitToken(String token) {
        return commonLogoutHelperService.validateUserExitQR(token);
    }

    public boolean validateUserPrincipal(MyUserPrincipal principal){
        if (principal.isAccountNonExpired() && principal.isAccountNonLocked() && principal.isEnabled()) {
            return true;
        }
        return false;
    }

    public void logoutUser(long entryNo) throws Exception {
        commonLogoutHelperService.logoutUser(entryNo);
    }
}


