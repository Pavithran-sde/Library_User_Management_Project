package com.LibraryManagement.LibraryUserManagement.User.Controller;


import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortBookingDto.ChargingPortBookingRequestDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortBookingDto.ClearChargingPortBookingDto;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.*;
import com.LibraryManagement.LibraryUserManagement.User.Services.ChargingPortBookingService;
import com.LibraryManagement.LibraryUserManagement.User.Services.GeoFencing;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/chargingPortBooking")
public class ChargePortBookingController {


    @Autowired
    private ChargingPortBookingService chargingPortBookingService;

    @Autowired
    private GeoFencing geoFencing;



    @GetMapping("/getJson")
    public ChargingPortBookingRequestDto getChargingPortBookingRequestDtoJson(){
        return new ChargingPortBookingRequestDto();
    }

    @GetMapping("/getBookingCount")
    public String getBookingCount(){
        return chargingPortBookingService.getBookingCount();
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllBookings(){
        try{
            return new ResponseEntity<>(chargingPortBookingService.getAllBookings(), HttpStatus.OK) ;
        } catch(NoContentFoundException e ){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (Exception e ){
            return ResponseEntity.internalServerError().body("Something went Wrong, Please try again");
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable long id){
        try{
            return new ResponseEntity<>(chargingPortBookingService.getBookingById(id), HttpStatus.OK) ;
        } catch(NotFoundException e ){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e ){
            return ResponseEntity.internalServerError().body("Something went Wrong, Please try again");
        }

    }

    @GetMapping("/getBookingsByUser/{uid}")
    public ResponseEntity<?> getBookingsByUserId(@PathVariable long uid){
        try{
            return new ResponseEntity<>(chargingPortBookingService.getBookingsByUserId(uid), HttpStatus.OK);
        } catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NoContentFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went Wrong, Please try again");
        }
    }

    @GetMapping("/getBookingsByChargingPort/{cpid}")
    public ResponseEntity<?> getBookingsByChargingPortId(@PathVariable long cpid) {
        try {
            return new ResponseEntity<>(chargingPortBookingService.getBookingsByChargingPortId(cpid), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NoContentFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        }  catch (Exception e){
            return ResponseEntity.internalServerError().body("Something went Wrong, Please try again");
        }
    }

    @PostMapping("/addBooking")
    public ResponseEntity<?> addBooking(@RequestBody ChargingPortBookingRequestDto bookingRequestDto){
        try{
            boolean isNearByChargingPort = geoFencing.isUserNearResource(bookingRequestDto.getUserLatitude(), bookingRequestDto.getUserLongitude(),12.9859584, 80.1406976, 5); //13.0843007, 80.2704622, 5);
            if(isNearByChargingPort)
                return new ResponseEntity<>(chargingPortBookingService.bookChargingPort(bookingRequestDto), HttpStatus.CREATED);
            else throw new NotNearResourceException("You need to be near the Charging Port to make a booking");
        } catch (NotNearResourceException e ) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NotFoundException e ){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (HasActiveBookingException | InvalidInput e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceAlreadyBooked | AllResourcesBookedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong, Please try again");
        }
    }


    @PutMapping("/clearBooking")
    public ResponseEntity<?> clearBooking(@RequestBody ClearChargingPortBookingDto clearChargingPortBookingDto) {
        try {
            return new ResponseEntity<>(chargingPortBookingService.clearChargingPortBooking(clearChargingPortBookingDto), HttpStatus.OK);
        } catch (NoActiveBookingException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch (InvalidInput e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong, please try again");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteBookingBeforeDate(@PathVariable LocalDateTime localDateTime){
        try{
            chargingPortBookingService.deleteBookingsBeforeDate(localDateTime);
            return new ResponseEntity<>("Successfully Deleted", HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Something went wrong, please try again");
        }
    }





}
