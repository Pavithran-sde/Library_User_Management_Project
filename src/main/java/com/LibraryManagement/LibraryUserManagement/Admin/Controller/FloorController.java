package com.LibraryManagement.LibraryUserManagement.Admin.Controller;


import com.LibraryManagement.LibraryUserManagement.Admin.DTO.Floor.FloorRequestDto;
import com.LibraryManagement.LibraryUserManagement.Admin.Service.FloorService;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/floor")
public class FloorController {

    @Autowired
    private FloorService floorService;


    @GetMapping("/getJson")
    public FloorRequestDto getJson(){
        return new FloorRequestDto();
    }


    @GetMapping("/")
    public ResponseEntity<?> getAllFloorDetails(){
        try{
            return new ResponseEntity<>(floorService.getAllFloors(), HttpStatus.FOUND);
        } catch(NoContentFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/getByFloorName/{floorName}")
    public ResponseEntity<?> getFloorDetailsByFloorName(@PathVariable String floorName){
        try{

            return new ResponseEntity<>(floorService.getFloorDetailsByFloorName(floorName), HttpStatus.FOUND);
        } catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/getByFloorNameAndWing")
    public ResponseEntity<?> getFloorDetailsByFloorNameAndWing(@RequestParam String floorName, @RequestParam String wing){
        try{

            return new ResponseEntity<>(floorService.getFloorDetailsByFloorNameAndWing(floorName, wing), HttpStatus.FOUND);
        } catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/admin/addFloor")
    public ResponseEntity<?> addFloor(@RequestBody @Valid FloorRequestDto floorRequestDto){
        try{
            return new ResponseEntity<>(floorService.addFloor(floorRequestDto), HttpStatus.CREATED);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/admin/updateFloor")
    public ResponseEntity<?> updateFloorByFloorName(@RequestBody FloorRequestDto floorRequestDto) {
        try {
            return new ResponseEntity<>(floorService.updateFloorByFloorName(floorRequestDto), HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @DeleteMapping("/admin/deleteFloor")
    public ResponseEntity<?> deleteFloorByFloorName(@RequestParam String floorName, @RequestParam String wing){
            try{
                floorService.deleteFloorByFloorName(floorName, wing);
                return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
            } catch( NotFoundException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }catch( Exception e){
                e.printStackTrace();
                return ResponseEntity.internalServerError().body(e.getMessage());
            }
        }
    }


