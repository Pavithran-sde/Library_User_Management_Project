package com.LibraryManagement.LibraryUserManagement.Common.Utilities.Services;

import com.LibraryManagement.LibraryUserManagement.Admin.Repository.FloorRepository;
import com.LibraryManagement.LibraryUserManagement.Common.UtilitiesDto.ResourceAvailabilityDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.ChargingPort;
import com.LibraryManagement.LibraryUserManagement.User.Entities.Tables;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import com.LibraryManagement.LibraryUserManagement.User.Repository.ChargingPortRepository;
import com.LibraryManagement.LibraryUserManagement.User.Repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ResourceAvailabilityFinderService {

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private ChargingPortRepository chargingPortRepository;

    @Autowired
    private FloorRepository floorRepository;

    private List<Tables> getTableList(String floorName){
        List<Long> floorIds = floorRepository.findByFloorName(floorName).stream().map(floor -> {
            return floor.getId();
        }).toList();
        List<Tables> tablesList = tableRepository.findByFloorIdIn(floorIds);
        if(tablesList.isEmpty()){
            return null;
        }
        return tablesList;
    }

    public long getTotalTables(String floorName){
        List<Tables> tablesList= getTableList(floorName);
        if(tablesList == null)
            return 0;
        return tablesList.size();
    }

    public long getAvailableTables(String floorName){
        List<Tables> tablesList = getTableList(floorName);
        long availableTable = 0;
        if(tablesList == null)
            return 0;
        for(Tables tables : tablesList){
            if(tables.getBookingStatus().equals(BookingStatusEnum.AVAILABLE.toString())){
                availableTable++;
            }
        }
        return availableTable;
    }

    public long getOccupiedTables(String floorName){
        return getTotalTables(floorName) - getAvailableTables(floorName);
    }

    public List<ChargingPort> getCpList(String floorName){
        List<Long> floorIds = floorRepository.findByFloorName(floorName).stream().map(floor -> {
            return floor.getId();
        }).toList();
        List<ChargingPort> CpList = chargingPortRepository.findByFloorIdIn(floorIds);
        if(CpList.isEmpty())
            return null;

        return CpList;
    }

    public long getTotalCp(String floorName){
        List<ChargingPort> cpList = getCpList(floorName);
        if(cpList == null)
            return 0;

        return cpList.size();
    }

    public long getAvailableCp(String floorName){
        List<ChargingPort> cpList = getCpList(floorName);
        if(cpList == null)
            return 0;

        long availableCp = 0;
        for(ChargingPort cp : cpList){
            if(cp.getBookingStatus().equals(BookingStatusEnum.AVAILABLE.toString())){
                availableCp++;
            }
        }
        return  availableCp;
    }

    public long getOccupiedCp(String floorName){
        return getTotalCp(floorName) - getAvailableCp(floorName);
    }

    public ResourceAvailabilityDto getDetails(String floorName){
        ResourceAvailabilityDto dto = new ResourceAvailabilityDto();
        long total = 0;
        long available = 0;
        total = getTotalTables(floorName);
        available = getAvailableCp(floorName);
        dto.setTotalTable(total);
        dto.setAvailableTable(available);
        dto.setOccupiedTable(total  - available);
        total = getTotalCp(floorName);
        available = getAvailableCp(floorName);
        dto.setTotalCp(total);
        dto.setAvailableCp(available);
        dto.setOccupiedCp(total - available);
        return dto;
    }
}
