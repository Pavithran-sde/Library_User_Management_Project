package com.LibraryManagement.LibraryUserManagement.User.Exceptions;

public class NotFoundException extends Exception {

    public NotFoundException(){
        super("No Such Record Found");
    }

    public NotFoundException(String message){
        super(message);
    }
}
