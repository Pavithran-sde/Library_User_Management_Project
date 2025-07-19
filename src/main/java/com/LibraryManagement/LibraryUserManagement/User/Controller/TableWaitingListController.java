package com.LibraryManagement.LibraryUserManagement.User.Controller;


import com.LibraryManagement.LibraryUserManagement.User.DTO.TableWaitingList.TableWaitingRequestDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.WaitingList_Table;
import com.LibraryManagement.LibraryUserManagement.User.Enum.WaitingListEnum;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Repository.TableWaitingListRepository;
import com.LibraryManagement.LibraryUserManagement.User.Services.TableWaitingListService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/tableWaitingList")
public class TableWaitingListController {

    @Autowired
    private TableWaitingListService tableWaitingListService;
    @Autowired
    private TableWaitingListRepository tableWaitingListRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllWaitingList(){
        try{
            return new ResponseEntity<>(tableWaitingListService.getAllWL(), HttpStatus.OK);
        } catch(NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/get")
    public WaitingList_Table get(){
        return tableWaitingListRepository.findFirstByWaitingListStatusOrderByWLentryTimeAsc(WaitingListEnum.WAITLISTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWaitingListById(@PathVariable long id){
        try{
            return new ResponseEntity<>(tableWaitingListService.getWLById(id), HttpStatus.OK);
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
            return ResponseEntity.ok().body(tableWaitingListService.getWlByUserId(id));
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
    public ResponseEntity<?> clearWLByUserId(@RequestBody TableWaitingRequestDto tableWaitingRequestDto){
        try{
            return ResponseEntity.ok(tableWaitingListService.clearWLByUserId(tableWaitingRequestDto));
        } catch(NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch(NoContentFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getFirstUser")
    public ResponseEntity<?> getFirstUserFromWL(){
        try{
            return new ResponseEntity<>(tableWaitingListService.getUserToAllocateTable(), HttpStatus.OK);
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
            return new ResponseEntity<>(tableWaitingListService.getWLSize(), HttpStatus.OK);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
