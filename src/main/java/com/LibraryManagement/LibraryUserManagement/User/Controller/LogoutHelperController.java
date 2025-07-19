package com.LibraryManagement.LibraryUserManagement.User.Controller;


import com.LibraryManagement.LibraryUserManagement.Admin.DTO.EntryLogDtos.LibraryExitDto;
import com.LibraryManagement.LibraryUserManagement.Common.Utilities.Services.QRGenerationService;
import com.LibraryManagement.LibraryUserManagement.Common.Utilities.Services.SecurityUtilService;
import com.LibraryManagement.LibraryUserManagement.SecurityConfigurations.MyUserPrincipal;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Services.LogoutHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/logoutHelper")
public class LogoutHelperController {

    @Autowired
    private SecurityUtilService securityUtilService;

    @Autowired
    private LogoutHelperService logoutHelperService;

    @Autowired
    private QRGenerationService qrGenerationService;


    @GetMapping("/getLogoutMethod")
    public ResponseEntity<?> getLogoutMethod(){
        try{
            MyUserPrincipal principal =  securityUtilService.getCurrentUser();
           boolean isValid =  logoutHelperService.validateUserPrincipal(principal);
           if(isValid){
               Map<String, String> response =  logoutHelperService.getMethodToLogout(principal.getUserId());
               return new ResponseEntity<>(response, HttpStatus.OK);
           }else{
               return ResponseEntity.badRequest().body("User is Blocked");
           }
           } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @PostMapping("/getQR")
    public ResponseEntity<?> getExitQR(@RequestBody Map<String, String> payload){
        String token = payload.get("token");
        try{
           byte[] qrImage = qrGenerationService.generateQRCodeImage(token);
           String qrBase64 = Base64.getEncoder().encodeToString(qrImage);

           //JSON response with QR image data URI and the token
           Map<String, String> response = Map.of(
                   "qrImage", "data:image/png;base64," + qrBase64,
                   "token", token
           );

          return ResponseEntity.ok(response);

       } catch (Exception e){
           e.printStackTrace();
           return ResponseEntity.internalServerError().body("unable to generate exit QR");
       }
    }


    @PostMapping("/scanLibraryExitQR")
    public ResponseEntity<?> scanLibraryExitQR(@RequestBody Map<String, String> payload){
        String token = payload.get("token");
        String id = payload.get("libraryEntryId");
        long libraryEntryId = Long.parseLong(id);
        try{
            LibraryExitDto dto = logoutHelperService.validateLibraryUserExitToken(token);
            dto.setEntryNumber(libraryEntryId);
            if(dto.isValid()){
               LibraryExitDto responseDto = logoutHelperService.logoutUser(dto.getEntryNumber());
                return ResponseEntity.ok(responseDto);
            } else{
                return ResponseEntity.badRequest().body("Invalid User");
            }

        } catch (NotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


        @PostMapping("/checkLogoutStatus")
        public ResponseEntity<?> getLogoutStatus(@RequestBody Map<String, String> payload){
            String token = payload.get("token");
            long libraryEntryId = logoutHelperService.getLibraryEntryId(token);

            try{
                LibraryExitDto responseDto = logoutHelperService.checkLogoutStatus(libraryEntryId);
                if(responseDto.isValid()){
                    return ResponseEntity.ok(responseDto);
                }else{
                    return ResponseEntity.badRequest().body("Not logged out yet");
                }
            } catch (NotFoundException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            catch( Exception e){
                e.printStackTrace();
                return ResponseEntity.internalServerError().body(e.getMessage());
            }
        }


}
