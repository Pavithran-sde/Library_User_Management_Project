package com.LibraryManagement.LibraryUserManagement.User.Exceptions;

public class NoActiveBookingException extends Exception{

    public NoActiveBookingException(String resource){
        super("This account has no active booking on "+resource);
    }
}
