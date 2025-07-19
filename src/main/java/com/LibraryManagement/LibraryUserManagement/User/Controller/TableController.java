package com.LibraryManagement.LibraryUserManagement.User.Controller;


import com.LibraryManagement.LibraryUserManagement.User.DTO.TableDtos.TableInfoDto;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.HasActiveBookingException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Mappers.TableMapper;
import com.LibraryManagement.LibraryUserManagement.User.Services.TableServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/table")
public class TableController {

    @Autowired
    private TableServices tableServices;

    @Autowired
    private TableMapper tableMapper;

    @GetMapping("/getJson")
    public TableInfoDto getTableInfoJson(){
        return new TableInfoDto();
    }


    @GetMapping("/")
    public ResponseEntity<?> getAllTableInfo(){
            try{
                return new ResponseEntity<>(
                        tableServices.getAllTables().stream().map(tables -> {
                        return tableServices.getTableWithStatusByTable(tables);
                        }), HttpStatus.FOUND);
            } catch (NoContentFoundException e) {
                return new ResponseEntity<>(Map.of("Message", e.getMessage()), HttpStatus.NO_CONTENT);
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();
            }
        }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTableById(@PathVariable long id){
        try{
            return new ResponseEntity<>(
                    tableMapper.toDto(tableServices.getTableById(id)),
                    HttpStatus.FOUND);
        }catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
           return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }


    @PostMapping("/addTable")
    public ResponseEntity<?> addTable(@RequestBody TableInfoDto tableInfoDto){
        try{
            return new ResponseEntity<>(
                    tableServices.addTable(tableInfoDto),
                    HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteTable/{id}")
    public ResponseEntity<?> deleteTableById(@PathVariable long id){
        try{
            tableServices.deleteTableById(id);
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
