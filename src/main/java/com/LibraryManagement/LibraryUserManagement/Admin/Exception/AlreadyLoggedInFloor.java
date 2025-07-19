package com.LibraryManagement.LibraryUserManagement.Admin.Exception;

public class AlreadyLoggedInFloor extends RuntimeException {

    public AlreadyLoggedInFloor() {
        super("You have not logged out of the previous floor!!");
    }
}
