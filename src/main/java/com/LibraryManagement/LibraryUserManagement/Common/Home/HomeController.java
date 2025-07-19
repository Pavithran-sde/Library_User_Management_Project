package com.LibraryManagement.LibraryUserManagement.Common.Home;


import com.LibraryManagement.LibraryUserManagement.Common.Utilities.Services.SecurityUtilService;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @Autowired
    private SecurityUtilService securityUtilService;

    @Autowired
    private UserRepository repository;

    @GetMapping("/")
    public String getHome(){
        try{
            User user = repository.findById(securityUtilService.getCurrentUser().getUserId())
                    .orElseThrow(() -> new NotFoundException("No such user found - home controller"));
            return "Welcome "+ user.getName();
        } catch (Exception e){
            return e.getMessage();
        }
    }

}
