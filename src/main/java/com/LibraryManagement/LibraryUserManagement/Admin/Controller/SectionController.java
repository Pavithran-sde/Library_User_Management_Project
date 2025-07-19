package com.LibraryManagement.LibraryUserManagement.Admin.Controller;


import com.LibraryManagement.LibraryUserManagement.Admin.DTO.SectionDto.SectionRequestDto;
import com.LibraryManagement.LibraryUserManagement.Admin.Service.SectionService;
import com.LibraryManagement.LibraryUserManagement.User.Enum.SectionAvailablityEnum;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/section")
public class SectionController {

    @Autowired
    private SectionService sectionService;


    @GetMapping("/getAllSections")
    public ResponseEntity<?> getAllSections(){
        try{
            return new ResponseEntity<>(sectionService.getAllSections(), HttpStatus.OK);
        } catch(NoContentFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @GetMapping("/getSectionsByName")
    public ResponseEntity<?> getSectionsByName(@RequestParam String sectionName){

        try{
            return new ResponseEntity<>(sectionService.getSectionByName(sectionName), HttpStatus.OK);
        } catch(NotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @PostMapping("/admin/addSection")
    public ResponseEntity<?> addSection(@RequestBody @Valid SectionRequestDto sectionRequestDto){
        try{
            return new ResponseEntity<>(sectionService.addSection(sectionRequestDto), HttpStatus.CREATED);
        } catch (NotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/admin/changeSectionFloor")
    public ResponseEntity<?> changeSectionFloor(@RequestParam String sectionName,
                                                @RequestParam String floorName,
                                                @RequestParam String wing){

        try{
            return new ResponseEntity<>(sectionService.changeSectionFloor(sectionName, floorName, wing), HttpStatus.OK);
        } catch (NotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/admin/changeAvailabilityOfSection")
    public ResponseEntity<?> changeAvailabilityOfSection(@RequestParam String sectionName,
                                                        @RequestParam SectionAvailablityEnum sectionAvailablityEnum) {
        try {
            return new ResponseEntity<>(sectionService.changeAvailabilityOfSection(sectionName, sectionAvailablityEnum), HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @DeleteMapping("/admin/deleteSection")
    public ResponseEntity<?> deleteSectionByName(@RequestParam String sectionName){
        try{
            sectionService.deleteSectionByName(sectionName);
            return new ResponseEntity<>("Successfully Deleted", HttpStatus.OK);
        }catch (NotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch( Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


}



