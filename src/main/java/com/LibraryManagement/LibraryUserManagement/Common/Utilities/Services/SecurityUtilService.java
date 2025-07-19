package com.LibraryManagement.LibraryUserManagement.Common.Utilities.Services;

import com.LibraryManagement.LibraryUserManagement.SecurityConfigurations.MyUserPrincipal;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtilService {

    public MyUserPrincipal getCurrentUser() throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserPrincipal principal = (MyUserPrincipal) auth.getPrincipal();

        if(principal != null){
          return principal;
        }
        else{
            throw new NotFoundException("Something went wrong in security util / get current user");
        }
    }

}
