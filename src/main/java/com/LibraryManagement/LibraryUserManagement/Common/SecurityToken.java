package com.LibraryManagement.LibraryUserManagement.Common;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/securityToken")
public class SecurityToken {

    @GetMapping("/getCSRFToken")
    public CsrfToken getCsrfToken(CsrfToken token){
        return token;
    }

}
