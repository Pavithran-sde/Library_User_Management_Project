package com.LibraryManagement.LibraryUserManagement.User.Exceptions;

import jakarta.persistence.criteria.CriteriaBuilder;

public class InvalidInput extends Exception {
    public InvalidInput(String message)
    {
        super(message);
    }

    public InvalidInput(){
        super();
    }
}
