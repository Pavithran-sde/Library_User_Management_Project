package com.LibraryManagement.LibraryUserManagement.Admin.Controller;

import com.LibraryManagement.LibraryUserManagement.Admin.Service.LibraryEntryService;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/libraryEntryLog")
public class LibraryEntryLogController {

    @Autowired
    private LibraryEntryService libraryEntryService;

    @GetMapping("/admin/getAllLibraryEntryLogs")
    public ResponseEntity<?> getAllLibraryEntryLog(){
        try{
            return ResponseEntity.ok(libraryEntryService.getAllLibraryEntryLogs());
        } catch (NoContentFoundException e){
            return ResponseEntity.ok(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
