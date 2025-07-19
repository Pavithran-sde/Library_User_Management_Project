package com.LibraryManagement.LibraryUserManagement.User.Exceptions;

public class HasActiveBookingException extends Exception{

    public HasActiveBookingException(String resource){
        super(resource + " has active booking.");
    }
}
