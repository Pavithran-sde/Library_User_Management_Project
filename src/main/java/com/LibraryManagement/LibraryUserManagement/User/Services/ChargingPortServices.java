package com.LibraryManagement.LibraryUserManagement.User.Services;


import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Floor;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.FloorRepository;
import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortDto.ChargingPortWithStatusDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortDto.ChargingPortDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.ChargingPort;
import com.LibraryManagement.LibraryUserManagement.User.Entities.ChargingPortBooking;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.HasActiveBookingException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Mappers.ChargingPortMapper;
import com.LibraryManagement.LibraryUserManagement.User.Repository.ChargingPortBookingRepository;
import com.LibraryManagement.LibraryUserManagement.User.Repository.ChargingPortRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ChargingPortServices {

    @Autowired
    private ChargingPortMapper chargingPortMapper;

    @Autowired
    private ChargingPortRepository chargingPortRepository;

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private ChargingPortBookingRepository chargingPortBookingRepository;


    
    public List<ChargingPort> getAllChargingPorts() throws NoContentFoundException {

        List<ChargingPort> chargingPorts =  chargingPortRepository.findAll();
        if(chargingPorts.size() == 0){
            throw new NoContentFoundException();
        }else{
            return chargingPorts;
        }
    }

    public ChargingPort getChargingPortById(long id) throws NotFoundException, Exception {
        return chargingPortRepository.findById(id).orElseThrow(() -> new NotFoundException());
    }

    public ChargingPortWithStatusDto addChargingPort(ChargingPortDto chargingPortDto) throws Exception {
        Floor floor = floorRepository.findByFloorNameAndWing(chargingPortDto.getFloorName(), chargingPortDto.getWing());
        if(floor == null){
            throw new NotFoundException("No such floor exist to add a charging Port");
        }
        ChargingPort chargingPort = new ChargingPort();
        chargingPort.setFloor(floor);
        chargingPort = chargingPortRepository.save(chargingPort);
        return getChargingPortWithStatusById(chargingPort.getId());
    }

    public void deleteChargingPortById(long id) throws NotFoundException, Exception {
        getChargingPortById(id);
        //check if the charging port has any active booking if present then don't delete else delete
        boolean hasActiveBooking  = chargingPortBookingRepository.existsByChargingPortIdAndStatus(id, BookingStatusEnum.BOOKED);
        if(!hasActiveBooking)
            chargingPortRepository.deleteById(id);
        else
            throw new HasActiveBookingException("Charging port");
    }

    public ChargingPortWithStatusDto getChargingPortWithStatusById(long id) throws NotFoundException, Exception {
        ChargingPortWithStatusDto chargingPort = new ChargingPortWithStatusDto();
        ChargingPort existingChargingPort = getChargingPortById(id);
        chargingPort.setId(id);
        chargingPort.setWing(existingChargingPort.getFloor().getWing());
        chargingPort.setFloorName(existingChargingPort.getFloor().getFloorName());
        chargingPort.setStatus(existingChargingPort.getStatus());
        return chargingPort;
    }

    public ChargingPortWithStatusDto getChargingPortWithStatusByChargingPort(ChargingPort existingChargingPort) {
        ChargingPortWithStatusDto ChargingPort = new ChargingPortWithStatusDto();
        ChargingPort.setId(existingChargingPort.getId());
        ChargingPort.setWing(existingChargingPort.getFloor().getWing());
        ChargingPort.setFloorName(existingChargingPort.getFloor().getFloorName());
        ChargingPort.setStatus(existingChargingPort.getStatus());
        return ChargingPort;
    }

}
