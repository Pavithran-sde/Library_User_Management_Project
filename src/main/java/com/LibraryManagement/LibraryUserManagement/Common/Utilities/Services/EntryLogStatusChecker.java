package com.LibraryManagement.LibraryUserManagement.Common.Utilities.Services;

import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Floor;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.FloorEntryLog;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.LibraryEntryLog;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.FloorEntryLogRepository;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.FloorRepository;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.LibraryEntryLogRepository;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntryLogStatusChecker {

    @Autowired
    private LibraryEntryLogRepository  libraryEntryLogRepository;

    @Autowired
    private FloorEntryLogRepository floorEntryLogRepository;

    @Autowired
    private FloorRepository floorRepository;



    public boolean getLibraryEntryStatus(long userId){

        LibraryEntryLog existingEntryLog = libraryEntryLogRepository.findByUserIdAndLoggedOutTime(userId, null);

        if(existingEntryLog != null) return true;
        else return false;
    }

    public boolean getFloorEntryStatus(long userId, long floorId) throws  Exception{

        FloorEntryLog floorEntryLog = floorEntryLogRepository.findByUserIdAndLoggedOutTime(userId, null);
        Floor floor = floorRepository.findById(floorId).orElseThrow(() -> new NotFoundException("No c=such floor exist"));

        if(floorEntryLog != null && floor != null)
            if(floorEntryLog.getFloor().equals(floor))
                 return true;

        return false;

    }

    public boolean getBothLibraryAndFloorStatus(long userId, long floorId) throws Exception{
        return getLibraryEntryStatus(userId) && getFloorEntryStatus(userId, floorId);
    }

}
