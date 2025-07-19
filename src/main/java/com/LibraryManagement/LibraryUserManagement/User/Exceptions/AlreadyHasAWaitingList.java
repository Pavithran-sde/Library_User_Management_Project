package com.LibraryManagement.LibraryUserManagement.User.Exceptions;

public class AlreadyHasAWaitingList extends Exception {
    public AlreadyHasAWaitingList(String resource) {
      super("You already have a active waiting List for a "+ resource);
    }
}
