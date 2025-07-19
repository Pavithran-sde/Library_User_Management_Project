package com.LibraryManagement.LibraryUserManagement.User.Exceptions;

public class ResourceAlreadyBooked extends Exception{

    public ResourceAlreadyBooked(long id, String resource){
        super("The "+ resource+" with id : "+id+" has already been Booked.");
    }

}
