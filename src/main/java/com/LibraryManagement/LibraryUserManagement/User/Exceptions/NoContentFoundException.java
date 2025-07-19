package com.LibraryManagement.LibraryUserManagement.User.Exceptions;

public class NoContentFoundException extends Exception {

    public NoContentFoundException(){
        super("No Content Found");
    }

    public NoContentFoundException(String message){
        super(message);
    }

}
