package com.LibraryManagement.LibraryUserManagement.Admin.Controller;


import com.LibraryManagement.LibraryUserManagement.Admin.DTO.Subjects.*;
import com.LibraryManagement.LibraryUserManagement.Admin.Service.SubjectService;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/")
    public ResponseEntity<?> getAllSubjects(){
        try{
            return new ResponseEntity<>(subjectService.getAllSubjects(), HttpStatus.OK);
        }catch(NoContentFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/getBySubjectName/{subjectName}")
    public ResponseEntity<?> getSubjectBySubjectName(@PathVariable String subjectName){
        try{
            return new ResponseEntity<>(subjectService.getSubjectByName(subjectName),HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @GetMapping("/getAllAvailableSubjects")
    public ResponseEntity<?> getAllAvailableSubjects(){
        try{
            return new ResponseEntity<>(subjectService.getAllAvailableSubjects(),HttpStatus.OK);
        } catch (NoContentFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }


    @PostMapping("/admin/addSubject")
    public ResponseEntity<?> addSubject(@RequestBody @Valid SubjectRequestDto subjectRequestDto){
        try{
            return new ResponseEntity<>(subjectService.addSubject(subjectRequestDto), HttpStatus.CREATED);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/admin/updateSubject")
    public ResponseEntity<?> updateSubjectBySubjectName(@RequestBody @Valid SubjectRequestDto subjectRequestDto) {
        try {
            return new ResponseEntity<>(subjectService.updateSubjectBySubjectName(subjectRequestDto), HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @DeleteMapping("/admin/deleteSubject/{subjectName}")
    public ResponseEntity<?> deleteSubjectBySubjectName(@PathVariable String subjectName){
        try{
            subjectService.deleteSubjectBySubjectName(subjectName);
            return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
        } catch( NotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch( Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
