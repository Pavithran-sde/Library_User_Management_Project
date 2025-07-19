package com.LibraryManagement.LibraryUserManagement.User.Controller;

import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortBookingDto.ChargingPortWaitingListRequestDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.WaitingList_ChargingPort;
import com.LibraryManagement.LibraryUserManagement.User.Enum.WaitingListEnum;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Repository.ChargingPortWaitingListRepository;
import com.LibraryManagement.LibraryUserManagement.User.Services.ChargingPortWaitingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chargingPortWaitingList")
public class ChargingPortWaitingListController {


    @Autowired
    private ChargingPortWaitingListService chargingPortWaitingListService;
    @Autowired
    private ChargingPortWaitingListRepository chargingPortWaitingListRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllWaitingList(){
        try{
            return new ResponseEntity<>(chargingPortWaitingListService.getAllWL(), HttpStatus.OK);
        } catch(NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/get")
    public WaitingList_ChargingPort get(){
        return chargingPortWaitingListRepository.findFirstByWaitingListStatusOrderByWLentryTimeAsc(WaitingListEnum.WAITLISTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWaitingListById(@PathVariable long id){
        try{
            return new ResponseEntity<>(chargingPortWaitingListService.getWLById(id), HttpStatus.OK);
        } catch (NotFoundException e ){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getByUserId/{id}")
    public ResponseEntity<?> getWLByUserId(@PathVariable long id) {
        try {
            return ResponseEntity.ok().body(chargingPortWaitingListService.getWlByUserId(id));
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(NoContentFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        }   catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went Wrong, Please Try Again");
        }
    }

    @PutMapping("/clearWl")
    public ResponseEntity<?> clearWLByUserId(@RequestBody ChargingPortWaitingListRequestDto chargingPortWaitingListRequestDto){
        try{
            return ResponseEntity.ok(chargingPortWaitingListService.clearWLByUserId(chargingPortWaitingListRequestDto));
        } catch(NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(NoContentFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getFirstUser/{floorName}")
    public ResponseEntity<?> getFirstUserFromWL(@PathVariable String floorName){
        try{
            return new ResponseEntity<>(chargingPortWaitingListService.getUserToAllocateChargingPort(floorName), HttpStatus.OK);
        } catch(NoContentFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getWLSize")
    public ResponseEntity<?> getWLSize(){
        try{
            return new ResponseEntity<>(chargingPortWaitingListService.getWLSize(), HttpStatus.OK);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
