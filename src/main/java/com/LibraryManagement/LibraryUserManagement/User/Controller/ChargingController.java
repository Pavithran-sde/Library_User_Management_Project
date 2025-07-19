package com.LibraryManagement.LibraryUserManagement.User.Controller;


import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortDto.ChargingPortDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortDto.ChargingPortWithStatusDto;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.*;
import com.LibraryManagement.LibraryUserManagement.User.Services.ChargingPortBookingService;
import com.LibraryManagement.LibraryUserManagement.User.Services.ChargingPortServices;
import com.LibraryManagement.LibraryUserManagement.User.Services.ChargingPortWaitingListService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/chargingPort")
public class ChargingController {


    @Autowired
    private ChargingPortServices chargingPortServices;

    @Autowired
    private ChargingPortWaitingListService chargingPortWaitingListService;

    @Autowired
    private ChargingPortBookingService chargingPortBookingService;


    @GetMapping("/getJson")
    public ChargingPortDto getChargingPortInfoJson(){
        return new ChargingPortDto();
    }


    @GetMapping("/")
    public ResponseEntity<?> getAllChargingPortInfo(){
        try{
            return new ResponseEntity<>(
                    chargingPortServices.getAllChargingPorts().stream().map(chargingPorts -> {
                        return chargingPortServices.getChargingPortWithStatusByChargingPort(chargingPorts);
                    }), HttpStatus.FOUND);
        } catch (NoContentFoundException e) {
            return new ResponseEntity<>(Map.of("Message", e.getMessage()), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getChargingPortById(@PathVariable long id){
        try{
            return new ResponseEntity<>(
                    chargingPortServices.getChargingPortWithStatusById(id),
                    HttpStatus.FOUND);
        }catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }

    @PostMapping("/addChargingPort")
    public ResponseEntity<?> addChargingPort(@RequestBody ChargingPortDto chargingPortDto){
        try{
            ChargingPortWithStatusDto chargingPortWithStatusDto = chargingPortServices.addChargingPort(chargingPortDto);
            chargingPortWaitingListService.allocateChargingPort(
                    chargingPortServices.getChargingPortById(
                            chargingPortWithStatusDto.getId()),
                            chargingPortBookingService.getDuration(chargingPortDto.getFloorName(), "increaseTime"));
                chargingPortBookingService.adjustDuration(chargingPortDto.getFloorName());
            return new ResponseEntity<>(chargingPortServices.getChargingPortWithStatusById(chargingPortWithStatusDto.getId()), HttpStatus.CREATED);
        } catch (NotNearResourceException e ) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (NotFoundException e ){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (HasActiveBookingException | AlreadyHasAWaitingList e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceAlreadyBooked | AllResourcesBookedException | AllResourceInFloorAreBooked  e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong, Please try again");
        }
    }

    @DeleteMapping("/deleteChargingPort/{id}")
    public ResponseEntity<?> deleteChargingPortById(@PathVariable long id){
        try{
            chargingPortServices.deleteChargingPortById(id);
            return new ResponseEntity<>("Successfully Deleted", HttpStatus.OK);
        } catch (HasActiveBookingException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch( Exception e){
            return ResponseEntity.internalServerError().body("Something went wrong try again later");
        }
    }

}
