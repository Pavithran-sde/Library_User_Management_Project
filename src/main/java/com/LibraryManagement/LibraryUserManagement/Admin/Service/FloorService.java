package com.LibraryManagement.LibraryUserManagement.Admin.Service;


import com.LibraryManagement.LibraryUserManagement.Admin.DTO.Floor.FloorRequestDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.Floor.FloorResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Floor;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Sections;
import com.LibraryManagement.LibraryUserManagement.Admin.Mappers.FloorMapper;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.FloorRepository;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.SectionRepository;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class FloorService {

    @Autowired
    private FloorRepository floorRepository;
    @Autowired
    private FloorMapper floorMapper;
    @Autowired
    private SectionRepository sectionRepository;

public static final String SECRET = "e4a1d20a-89b4-45f6-9c91-bb43de4a8b99";

    public List<FloorResponseDto> getAllFloors() throws Exception{
       List<Floor> floorList =  floorRepository.findAllByIsSystemReserved(false);
       if(floorList.isEmpty()){
           throw new NoContentFoundException();
       }

       return getFloorDetails(floorList);
    }



    private List<FloorResponseDto> getFloorDetails(List<Floor> floorList){
        return floorList.stream().map(floor ->{
                return  floorMapper.toDto(floor);
            }).toList();
    }


    public List<FloorResponseDto> getFloorDetailsByFloorName(String floorName) throws Exception {
        List<Floor> floorList = floorRepository.findByFloorName(floorName);
        if(!floorList.isEmpty()){
            return getFloorDetails(floorList);
        }
        throw new NotFoundException();
    }

    public FloorResponseDto addFloor(FloorRequestDto dto) {
        Floor floor = floorMapper.fromDto(dto);
        String token  = getFloorToken(dto);
        floor.setToken(token);
        floor.setSecret(SECRET);
        floorRepository.save(floor);

        return floorMapper.toDto(floor);
    }

    private String getFloorToken(FloorRequestDto dto){

        Algorithm algorithm = Algorithm.HMAC256(SECRET);
       return JWT.create()
                .withIssuer("library-admin")
                .withClaim("purpose", "floor_entry")
                .withClaim("floorName", dto.getFloorName())
                .withClaim("wing", dto.getWing())
                .withClaim("openTime", dto.getOpenTime().toString())
                .withClaim("closeTime", dto.getCloseTime().toString())
                .withClaim("active", dto.isActive())
                .withClaim("rolesAllowed", dto.getRolesAllowed().toString())
                .withIssuedAt(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
                .sign(algorithm);
    }


    public FloorResponseDto updateFloorByFloorName(FloorRequestDto floorRequestDto) throws Exception{

        Floor existingFloor = floorRepository.findByFloorNameAndWing(floorRequestDto.getFloorName(), floorRequestDto.getWing());
        if(existingFloor != null){
            existingFloor.setToken(getFloorToken(floorRequestDto));
            existingFloor.setOpenTime(floorRequestDto.getOpenTime());
            existingFloor.setCloseTime(floorRequestDto.getCloseTime());
            existingFloor.setWing(floorRequestDto.getWing());
            existingFloor.setActive(floorRequestDto.isActive());
            existingFloor.setRolesAllowed(floorRequestDto.getRolesAllowed());
            existingFloor.setSystemReserved(floorRequestDto.isSystemReserved());
            floorRepository.save(existingFloor);
            return floorMapper.toDto(existingFloor);
        }
        throw new NotFoundException();

    }

    public void deleteFloorByFloorName(String floorName, String wing) throws  Exception {
        Sections section = sectionRepository.findByFloorId(getFloorDetailsByFloorNameAndWing(floorName, wing).getId());
        if(section != null){
            sectionRepository.delete(section);
        }
        floorRepository.deleteById(getFloorDetailsByFloorNameAndWing(floorName, wing).getId());
    }

    public FloorResponseDto getFloorDetailsByFloorNameAndWing(String floorName, String wing) throws Exception {
        Floor floor = floorRepository.findByFloorNameAndWing(floorName, wing);
        if(floor == null) throw new NotFoundException("No Such Floor Exist");
        return floorMapper.toDto(floor);
    }


}
