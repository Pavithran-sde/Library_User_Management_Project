package com.LibraryManagement.LibraryUserManagement.User.Exceptions;

public class AllResourcesBookedException extends Exception{

    public AllResourcesBookedException(String resource){
        super("All "+ resource + " are Booked you are being WaitListed");
    }

}
