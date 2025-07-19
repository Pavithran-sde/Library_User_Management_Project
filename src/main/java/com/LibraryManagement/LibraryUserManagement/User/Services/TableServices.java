package com.LibraryManagement.LibraryUserManagement.User.Services;

import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Floor;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.FloorRepository;
import com.LibraryManagement.LibraryUserManagement.User.DTO.TableDtos.TableInfoDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.TableDtos.TableWithStatusInfoDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.Tables;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.HasActiveBookingException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Mappers.TableMapper;
import com.LibraryManagement.LibraryUserManagement.User.Repository.TableBookingRepository;
import com.LibraryManagement.LibraryUserManagement.User.Repository.TableRepository;
import com.LibraryManagement.LibraryUserManagement.User.Repository.TableWaitingListRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TableServices {

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private TableWaitingListService tableWaitingListService;

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private TableBookingRepository tableBookingRepository;


    public List<Tables> getAllTables() throws NotFoundException, Exception {
        List<Tables> tables =  tableRepository.findAll();
        if(tables.size() == 0){
            throw new NoContentFoundException();
        }else{
            return tables;
        }
    }


    public Tables getTableById(long id) throws NotFoundException, Exception {
        return tableRepository.findById(id).orElseThrow(() -> new NotFoundException());
    }


    public TableWithStatusInfoDto addTable(TableInfoDto tableInfoDto) throws Exception {
        Floor floor = floorRepository.findByFloorNameAndWing(tableInfoDto.getFloorName(), tableInfoDto.getWing());
        if(floor == null){
            throw new NotFoundException("No such floor exist to add a table");
        }
        Tables tables = new Tables();
        tables.setFloor(floor);
        tables = tableRepository.save(tables);
         //if there is any waitlisted user to allocate the newly created table to the use
            try{
                tableWaitingListService.allocateTable(tables);
            } catch (Exception e){
                e.printStackTrace();
            }
         return getTableWithStatusById(tables.getId());
    }

    public void deleteTableById(long id) throws NotFoundException, Exception {
            getTableById(id);
            // add logic to check for any bookings available if present then don't delete else delete
       // 1. checking in table reservation
        boolean hasActiveBooking = tableBookingRepository.existsByTablesIdAndStatus(id, BookingStatusEnum.BOOKED);
        if(!hasActiveBooking)
            tableRepository.deleteById(id);
        else
            throw new HasActiveBookingException("Table");
    }

    public TableWithStatusInfoDto getTableWithStatusById(long id) throws NotFoundException, Exception {
            TableWithStatusInfoDto infoDto = new TableWithStatusInfoDto();
            Tables existingTable = getTableById(id);
            infoDto.setId(id);
        infoDto.setWing(existingTable.getFloor().getWing());
        infoDto.setFloorName(existingTable.getFloor().getFloorName());
        infoDto.setBookingStatusEnum(existingTable.getStatus());
        return infoDto;
    }

    public TableWithStatusInfoDto getTableWithStatusByTable(Tables existingTable) {
        TableWithStatusInfoDto infoDto = new TableWithStatusInfoDto();
        infoDto.setId(existingTable.getId());
        infoDto.setWing(existingTable.getFloor().getWing());
        infoDto.setFloorName(existingTable.getFloor().getFloorName());
        infoDto.setBookingStatusEnum(existingTable.getStatus());
        return infoDto;
    }

}
