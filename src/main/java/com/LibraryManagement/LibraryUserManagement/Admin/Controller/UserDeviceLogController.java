package com.LibraryManagement.LibraryUserManagement.Admin.Controller;

import com.LibraryManagement.LibraryUserManagement.Admin.DTO.UserDeviceLog.DeleteUserDeviceLogEntryDto;
import com.LibraryManagement.LibraryUserManagement.Admin.Service.UserDeviceLogService;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.UserDeviceLog.UserDeviceLogRequestDto;

@RestController
@RequestMapping("/admin/userDeviceLog")
public class UserDeviceLogController {

    @Autowired
    private UserDeviceLogService userDeviceLogService;

    @GetMapping("/getJson")
    public UserDeviceLogRequestDto getJson(){
        return new UserDeviceLogRequestDto();
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllDeviceLogs(){
        try{
            return new ResponseEntity<>(userDeviceLogService.getAllDeviceLogs(), HttpStatus.OK);
        } catch (NoContentFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getByLogId/{id}")
    public ResponseEntity<?> getAllDeviceLogsByLogId(@PathVariable long id){
        try{
            return new ResponseEntity<>(userDeviceLogService.getAllDeviceLogsById(id), HttpStatus.OK);
        } catch (NotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getByDeviceId/{id}")
    public ResponseEntity<?> getAllDeviceLogsByDeviceId(@PathVariable long id){
        try{
            return new ResponseEntity<>(userDeviceLogService.getAllDeviceLogsByDeviceId(id), HttpStatus.OK);
        } catch (NotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (NoContentFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping("/admin/addEntry")
    public ResponseEntity<?> addEntry(@RequestBody @Valid UserDeviceLogRequestDto requestDto){
        try{
            return new ResponseEntity<>(userDeviceLogService.addEntry(requestDto), HttpStatus.CREATED);
        } catch (NotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/admin/clearEntry")
    public ResponseEntity<?> clearEntry(@RequestBody @Valid UserDeviceLogRequestDto requestDto){
        try{
            return new ResponseEntity<>(userDeviceLogService.clearEntry(requestDto), HttpStatus.CREATED);
        } catch (NotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/admin/deleteLogEntryById")
    public ResponseEntity<?> deleteLogEntryById(@RequestBody DeleteUserDeviceLogEntryDto deleteDto){
        try{
            userDeviceLogService.deleteLogEntryById(deleteDto);
            return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
        } catch(NotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch( Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
