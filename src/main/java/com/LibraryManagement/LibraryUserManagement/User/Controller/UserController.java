package com.LibraryManagement.LibraryUserManagement.User.Controller;


import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto.UserCredentialDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto.UserInfoDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Entities.UserCredentials;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.InvalidCredentialsException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.InvalidInput;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Mappers.UserMapper;
import com.LibraryManagement.LibraryUserManagement.User.Services.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServices us;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/getJson")
    public User getJsonFormat(){
        return new User();
    }

    @GetMapping("/getCredentialJson")
    public UserCredentials getCredentialJsonFormat(){
        return new UserCredentials();
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllUsers(){
        try{
            return new ResponseEntity<>(us.getAllUsers(), HttpStatus.FOUND);
        } catch(NoContentFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable(name="id") long id){
        try{

            return new ResponseEntity<>(userMapper.toUserInfoDto(us.getUserById(id)), HttpStatus.FOUND);
        } catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/BookingStatus/{id}")
    public ResponseEntity<?> getUserBookingStatus(@PathVariable(name="id") long id){
        try{
            return new ResponseEntity<>(us.getUserBookingStatus(id), HttpStatus.FOUND);
        }catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody User user){
        try{
            return new ResponseEntity<>(userMapper.toUserInfoDto(us.addUser(user)), HttpStatus.CREATED);
        } catch(DataIntegrityViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }catch(InvalidInput e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(Map.of("Message :", "Something went wrong Try again later"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody UserInfoDto userInfoDto){

        try{
            return new ResponseEntity<>(us.updateUser(id, userInfoDto),HttpStatus.OK);
        } catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/updateUserCredentials/{id}")
    public ResponseEntity<?> updateUserCredentials(@PathVariable long id, @RequestBody UserCredentialDto userCredentialDto){

        try{
            us.updateUserCredentials(id, userCredentialDto);
            return ResponseEntity.ok("Successfully Updated User Credentials");
        } catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable long id, @RequestBody UserCredentialDto userCredentialDto){
        try {
            us.deleteUserById(id, userCredentialDto);
            return ResponseEntity.ok().body("Successfully Deleted");
        } catch (NotFoundException e){
            return ResponseEntity.badRequest().body(Map.of("message :", e.getMessage()));
        } catch (InvalidCredentialsException e){
            return ResponseEntity.badRequest().body(Map.of("Message :", e.getMessage()));
        } catch(Exception e){
            return ResponseEntity.internalServerError().body("Something went Wrong, Please try again");
        }
    }



}
