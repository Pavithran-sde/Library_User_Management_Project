package com.LibraryManagement.LibraryUserManagement.User.Services;

import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto.UserBookingStatusResponseDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto.UserCredentialDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto.UserInfoDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Entities.UserCredentials;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import com.LibraryManagement.LibraryUserManagement.User.Enum.UserBookingResponseEnum;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.InvalidCredentialsException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.InvalidInput;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Mappers.UserMapper;
import com.LibraryManagement.LibraryUserManagement.User.Repository.ChargingPortBookingRepository;
import com.LibraryManagement.LibraryUserManagement.User.Repository.TableBookingRepository;
import com.LibraryManagement.LibraryUserManagement.User.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServices {
    
    @Autowired
    private UserRepository ur;

    @Autowired
    private TableBookingRepository tbr;

    @Autowired
    private ChargingPortBookingRepository cpbr;

    @Autowired
    UserMapper mapper;
    
    
    public List<User> getAllUsers() throws Exception {
           return ur.findAll();
    }

    public User getUserById(long id) throws NotFoundException {
      return  ur.findById(id).orElseThrow(() -> new NotFoundException());
    }

    public UserBookingStatusResponseDto getUserBookingStatus(long id) throws NotFoundException {
        //check if such user exist
        getUserById(id);

        UserBookingStatusResponseDto response = new UserBookingStatusResponseDto();
        response.setTableBookingStatus(
                tbr.existsByUserIdAndStatus(id,
                        BookingStatusEnum.BOOKED) ? UserBookingResponseEnum.HAS_BOOKED.toString()
                        : UserBookingResponseEnum.NOT_BOOKED.toString());

        response.setChargingPortBookingStatus(
                cpbr.existsByUserIdAndStatus(id,
                        BookingStatusEnum.BOOKED) ? UserBookingResponseEnum.HAS_BOOKED.toString()
                        : UserBookingResponseEnum.NOT_BOOKED.toString());
        return response;
    }


    public User addUser(User user) throws Exception{
        if(user.getUserCredentials() != null){
            user.getUserCredentials().setUser(user);
           //user password encoded with bcrypt strength 13 and stored in database
            user.getUserCredentials().setUserPassword(passwordEncoder()
                    .encode(user.getUserCredentials().getUserPassword()));
            return ur.save(user);
        }
        throw new InvalidInput("No user credentials found to add user");
    }

    public UserInfoDto updateUser(long id, UserInfoDto userDto) throws NotFoundException {
            User existingUser = getUserById(id);
            existingUser.setName(userDto.getName());
            existingUser.setPhoneNo(userDto.getPhoneNo());
            existingUser.setEmail(userDto.getEmail());
            ur.save(existingUser);
            userDto.setId(existingUser.getId());
            return userDto;
    }

    public void updateUserCredentials (long id, UserCredentialDto userCredentialDto) throws NotFoundException {
            User existingUser = getUserById(id);
            UserCredentials updateUserCred = mapper.fromDto(userCredentialDto);
            existingUser.getUserCredentials().setUserName(userCredentialDto.getUserName());
            existingUser.getUserCredentials().setUserPassword(userCredentialDto.getUserPassword());
            ur.save(existingUser);
    }

    public void deleteUserById(long id, UserCredentialDto userCredentialDto) throws Exception {
            User existingUser = getUserById(id);
            String userName = existingUser.getUserCredentials().getUserName();
            String userPassword = existingUser.getUserCredentials().getUserPassword();
            if(userName.equals(userCredentialDto.getUserName())
                    && userPassword.equals(userCredentialDto.getUserPassword())){
                ur.deleteById(id);
            }else{
                throw new InvalidCredentialsException();
            }
    }


    private PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(13);
    }

}
