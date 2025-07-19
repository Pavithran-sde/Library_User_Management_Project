package com.LibraryManagement.LibraryUserManagement.User.Controller;


import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDevice.UserDeviceRequestDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto.UserDeviceDeleteRequestDto;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Services.UserDevicesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userDevice")
public class UserDeviceController {

    @Autowired
    private UserDevicesService userDevicesService;

    @GetMapping("/getJson")
    public UserDeviceRequestDto getJson(){
        return new UserDeviceRequestDto();
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllDevices(){
        try{
            return new ResponseEntity<>(userDevicesService.getAllDevices(), HttpStatus.OK);
        } catch (NoContentFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getDeviceById/{id}")
    public ResponseEntity<?> getDeviceById(@PathVariable long id){
        try{
            return new ResponseEntity<>(userDevicesService.getDeviceById(id), HttpStatus.OK);
        } catch (NotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getDeviceByUserId/{id}")
    public ResponseEntity<?> getDeviceByUserId(@PathVariable long id){
        try{
            return new ResponseEntity<>(userDevicesService.getDeviceByUserId(id), HttpStatus.OK);
        } catch (NotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/addDevice")
    public ResponseEntity<?> addDevice(@RequestBody @Valid UserDeviceRequestDto dto){
        try{
            return new ResponseEntity<>(userDevicesService.addDevice(dto), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/updateDevice/{id}")
    public ResponseEntity<?> updateDevice(@PathVariable long id, @RequestBody @Valid UserDeviceRequestDto dto){
        try{
            return new ResponseEntity<>(userDevicesService.updateDevice(id, dto), HttpStatus.OK);
        } catch (NotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }


    @DeleteMapping("/deleteDevice")
    public ResponseEntity<?> deleteDeviceById(@RequestBody UserDeviceDeleteRequestDto dto){
            try{
                userDevicesService.deleteDeviceById(dto);
                return new ResponseEntity<>("Successfully Deleted", HttpStatus.OK);
            } catch (NotFoundException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch(IllegalAccessException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            catch (Exception e){
                return ResponseEntity.internalServerError().build();
            }
    }


}
