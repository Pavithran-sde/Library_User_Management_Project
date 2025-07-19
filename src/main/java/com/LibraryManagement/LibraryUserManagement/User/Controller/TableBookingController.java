package com.LibraryManagement.LibraryUserManagement.User.Controller;


import com.LibraryManagement.LibraryUserManagement.User.DTO.TableBookingDto.ClearTableBookingDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.TableBookingDto.TableBookingRequestDto;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.*;
import com.LibraryManagement.LibraryUserManagement.User.Services.GeoFencing;
import com.LibraryManagement.LibraryUserManagement.User.Services.TableBookingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/tableBooking")
public class TableBookingController {

    @Autowired
    private TableBookingService tableBookingService;

    @Autowired
    private GeoFencing geoFencing;


    @GetMapping("/getTableBookingRequestDtoJson")
    public TableBookingRequestDto getTableBookingRequestDtoJson(){
        return new TableBookingRequestDto();
    }

    @GetMapping("/getBookingCount")
    public String getBookingCount(){
        return tableBookingService.getBookingCount();
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllBookings(){
        try{
              return new ResponseEntity<>(tableBookingService.getAllBookings(), HttpStatus.OK) ;
        } catch(NoContentFoundException e ){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (Exception e ){
            return ResponseEntity.internalServerError().body("Something went Wrong, Please try again");
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable long id){
        try{
            return new ResponseEntity<>(tableBookingService.getBookingById(id), HttpStatus.OK) ;
        } catch(NotFoundException e ){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e ){
            return ResponseEntity.internalServerError().body("Something went Wrong, Please try again");
        }

    }

    @GetMapping("/getBookingsByUser/{uid}")
    public ResponseEntity<?> getBookingsByUserId(@PathVariable long uid){
        try{
            return new ResponseEntity<>(tableBookingService.getBookingsByUserId(uid), HttpStatus.OK);
        } catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NoContentFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went Wrong, Please try again");
        }
    }

    @GetMapping("/getBookingsByTables/{tid}")
    public ResponseEntity<?> getBookingsByTablesId(@PathVariable long tid) {
        try {
            return new ResponseEntity<>(tableBookingService.getBookingsByTablesId(tid), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NoContentFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
            }  catch (Exception e){
            return ResponseEntity.internalServerError().body("Something went Wrong, Please try again");
        }
    }

    @PostMapping("/addBooking")
    public ResponseEntity<?> addBooking(@RequestBody TableBookingRequestDto bookingRequestDto){
        try{
            boolean isNearByTable = geoFencing.isUserNearResource(bookingRequestDto.getUserLatitude(), bookingRequestDto.getUserLongitude(), 12.9859584, 80.1406976,5);//13.0843007, 80.2704622, 5);
            if(isNearByTable)
                return new ResponseEntity<>(tableBookingService.bookTable(bookingRequestDto), HttpStatus.CREATED);
            else throw new NotNearResourceException("You need to be near the table to make a booking");
        } catch (NotNearResourceException e ) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NotFoundException e ){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (HasActiveBookingException | AlreadyHasAWaitingList | InvalidInput e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceAlreadyBooked | AllResourcesBookedException | AllResourceInFloorAreBooked  e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong, Please try again");
        }
    }

    @PutMapping("/clearBooking")
    public ResponseEntity<?> clearBooking(@RequestBody ClearTableBookingDto clearTableBookingDto) {
        try {
            return new ResponseEntity<>(tableBookingService.clearTableBooking(clearTableBookingDto), HttpStatus.OK);
        } catch (NoActiveBookingException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (InvalidInput e){
                return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
           e.printStackTrace();
           return ResponseEntity.internalServerError().body("Something went wrong, please try again");
       }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteBookingBeforeDate(@PathVariable LocalDateTime localDateTime){
        try{
            tableBookingService.deleteBookingsBeforeDate(localDateTime);
            return new ResponseEntity<>("Successfully Deleted", HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Something went wrong, please try again");
        }
    }

}
