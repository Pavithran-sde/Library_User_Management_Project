package com.LibraryManagement.LibraryUserManagement.Admin.Controller;

import com.LibraryManagement.LibraryUserManagement.Admin.DTO.EntryLogDtos.EntryResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.EntryLogDtos.LibraryExitDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.EntryLogDtos.SecurityLibraryExitDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.QR.FloorEntryQRScannerDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.QR.LibraryEntryQRScannerDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.UserDeviceLog.UserDeviceLogRequestDto;
import com.LibraryManagement.LibraryUserManagement.Admin.Exception.AlreadyLoggedInFloor;
import com.LibraryManagement.LibraryUserManagement.Admin.Service.FloorEntryLogService;
import com.LibraryManagement.LibraryUserManagement.Admin.Service.LibraryEntryService;
import com.LibraryManagement.LibraryUserManagement.Admin.Service.QrAdminService;
import com.LibraryManagement.LibraryUserManagement.Common.Utilities.Services.SecurityUtilService;
import com.LibraryManagement.LibraryUserManagement.SecurityConfigurations.MyUserPrincipal;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/adminQR")
public class QrAdminController {

    @Autowired
    private QrAdminService qrAdminService;

    @Autowired
    private LibraryEntryService libraryEntryService;

    @Autowired
    private SecurityUtilService securityUtilService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FloorEntryLogService floorEntryLogService;




    @GetMapping("/getEntryQR")
    public ResponseEntity<?> getEntryQR(){

        try {
            byte[] qrImage = qrAdminService.generateQrImageForToday();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrImage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("QR generation failed: " + e.getMessage());
        }

    }

    @PostMapping("/validateLibraryEntryQR/{token}")
    public ResponseEntity<?> validateEntryQR(@PathVariable String token){
        try {
            LibraryEntryQRScannerDto dto = qrAdminService.validateLibraryEntryToken(token);
            if(dto.isValid())
                return ResponseEntity.ok().body("You have successfully logged in");
            else
                return ResponseEntity.badRequest().body("Invalid or expired Qr code");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("QR generation failed: " + e.getMessage());
        }

    }


    @PostMapping("/scanLibraryEntryQR")
    public ResponseEntity<?> scanLibraryEntryQR(@RequestBody Map<String, String> payload ){
        String token = payload.get("token");
        try {
            MyUserPrincipal principal = securityUtilService.getCurrentUser();
            if(qrAdminService.validateUserPrincipal(principal)){
                LibraryEntryQRScannerDto dto = qrAdminService.validateLibraryEntryToken(token);
                if(dto.isValid()){
                    EntryResponseDto responseDto = libraryEntryService.makeLibraryEntry(securityUtilService.getCurrentUser().getId());
                    if (responseDto.getUser() != null){
                        return new ResponseEntity<>(responseDto, HttpStatus.OK);
                    }else{
                        return ResponseEntity.internalServerError().body("something went wrong in fetching user - scan qr admin");
                    }
                }else{
                    return ResponseEntity.badRequest().body(Map.of("message", "Scanned QR is Invalid"));
                }
            } else{
                throw new Exception("Invalid User");
            }
        } catch (NotFoundException | DataIntegrityViolationException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Scanned QR is Invalid."));
        }
    }


    @GetMapping("/getFloorQR")
    public ResponseEntity<?> getFloorQR(@RequestParam String floorName, @RequestParam String wing){
        try{
            byte[] qrImage = qrAdminService.getFloorQR(floorName, wing);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrImage);
        } catch(NotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/scanFloorEntryQR")
    public ResponseEntity<?> scanFloorEntryQR(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        try {
                    MyUserPrincipal principal = securityUtilService.getCurrentUser();
                    if(qrAdminService.validateUserPrincipal(principal)){
                    FloorEntryQRScannerDto dto = qrAdminService.validateFloorEntryToken(token, principal);
                    if(dto.isValid()){
                        EntryResponseDto responseDto=  floorEntryLogService.makeFloorEntry(securityUtilService.getCurrentUser().getUserId(), dto);
                        if (responseDto.getUser() != null){
                            return new ResponseEntity<>(responseDto, HttpStatus.OK);
                        }else{
                            return ResponseEntity.internalServerError().body("something went wrong in fetching Floor - scan qr admin");
                        }
                    }else{
                        return ResponseEntity.badRequest().body(Map.of("message", "Scanned QR is Invalid."));
                    }
            } else {
                throw new Exception("Invalid User");
            }

        } catch (NotFoundException | DataIntegrityViolationException | AlreadyLoggedInFloor e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Scanned QR is Invalid."));
        }
    }

//    @PostMapping("/library-entry/log-device")
//    public ResponseEntity<?> logDevice(@RequestBody UserDeviceLogRequestDto dto){
//        System.out.println("Device Id "+ dto.getDeviceId());
//        System.out.println("User Id "+ dto.getUserId());
//        return ResponseEntity.ok("will make entry in database");
//    }

    @PostMapping("/scanLibraryExitQR")
    public ResponseEntity<?> scanLibraryExitQR(@RequestBody Map<String, String> payload ){
        String token = payload.get("token");
        try {
            LibraryExitDto dto = qrAdminService.validateLibraryUserExitToken(token);
            if(dto.isValid()){
                SecurityLibraryExitDto responseDto = libraryEntryService.getDetailsOfEntry(dto.getEntryNumber());

                if (responseDto.getUser() != null){
                    return new ResponseEntity<>(responseDto, HttpStatus.OK);
                }else{
                    return ResponseEntity.internalServerError().body("something went wrong in fetching user - scan qr admin");
                }
            }else{
                return ResponseEntity.badRequest().body(Map.of("message", "Token is Invalid."));
            }
        } catch (DataIntegrityViolationException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token."));
        }
    }

    @PutMapping("/makeLibraryExit")
    public ResponseEntity<?> makeLibraryExit(@RequestBody Map<String, String> payload ){
        String id = payload.get("libraryEntryId");
        long libraryEntryId = Long.parseLong(id);
        try {
            qrAdminService.logoutUser(libraryEntryId);
            return ResponseEntity.ok().body("Successfully logged out of library");
        } catch (DataIntegrityViolationException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token."));
        }
    }
}
