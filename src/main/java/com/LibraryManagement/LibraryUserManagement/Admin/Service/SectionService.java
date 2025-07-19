package com.LibraryManagement.LibraryUserManagement.Admin.Service;


import com.LibraryManagement.LibraryUserManagement.Admin.DTO.Floor.FloorRecommendationResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.Recommendation.RecommendationResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.SectionDto.SectionRequestDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.SectionDto.SectionResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Floor;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Subjects;
import com.LibraryManagement.LibraryUserManagement.Admin.Mappers.FloorMapper;
import com.LibraryManagement.LibraryUserManagement.Admin.Mappers.RecommendationMapper;
import com.LibraryManagement.LibraryUserManagement.Admin.Mappers.SectionMapper;
import com.LibraryManagement.LibraryUserManagement.Admin.Mappers.SubjectMapper;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.FloorRepository;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.SectionRepository;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Sections;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.SubjectsRepository;
import com.LibraryManagement.LibraryUserManagement.Common.Utilities.Services.ResourceAvailabilityFinderService;
import com.LibraryManagement.LibraryUserManagement.Common.UtilitiesDto.ResourceAvailabilityDto;
import com.LibraryManagement.LibraryUserManagement.User.Enum.SectionAvailablityEnum;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SectionMapper sectionMapper;

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private FloorMapper floorMapper;

    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private SubjectsRepository subjectsRepository;

    @Autowired
    private RecommendationMapper recommendationMapper;

    @Autowired
    private ResourceAvailabilityFinderService availabilityService;

    public List<SectionResponseDto> getAllSections() throws Exception{
       List<Sections>  sectionsList = sectionRepository.findAll();
       if(sectionsList.isEmpty()){
           throw new NoContentFoundException("No Section found");
       }

       return getSectionDetails(sectionsList);
    }

    private List<SectionResponseDto> getSectionDetails(List<Sections> sectionsList) {

       return sectionsList.stream().map(sections -> {
            SectionResponseDto sectionResponseDto = sectionMapper.toDto(sections);
            sectionResponseDto.setFloorDetails(floorMapper.toDto(sections.getFloor()));
            return sectionResponseDto;
        }).toList();

    }


    public SectionResponseDto getSectionByName(String sectionName) throws Exception {
        Sections section = sectionRepository.findBySectionName(sectionName);
        if(section == null) throw new NotFoundException("No Such Section Found");
        SectionResponseDto sectionResponseDto = sectionMapper.toDto(section);
        sectionResponseDto.setFloorDetails(floorMapper.toDto(section.getFloor()));
        return sectionResponseDto;
    }


    @Transactional
    public SectionResponseDto changeSectionFloor(String sectionName, String floorName, String wing) throws Exception {

        Sections section = sectionRepository.findBySectionName(sectionName);
        if(section == null) throw new NotFoundException("No Such Section Exist");
        Floor floor = floorRepository.findByFloorNameAndWing(floorName, wing);
        if(floor == null) throw new NotFoundException("No Such Floor Exist");

        Floor dummy = floorRepository.findByIsSystemReserved(true);
        if(dummy == null) throw new NotFoundException("Cannot Change Floor, Try again");

        Floor originalFloor = section.getFloor();

        Sections existingSection = sectionRepository.findByFloorId(floor.getId());
        if(existingSection != null){
            existingSection.setFloor(dummy);
            sectionRepository.save(existingSection);
            sectionRepository.flush();
        }

        section.setFloor(floor);
        sectionRepository.save(section);


        if(existingSection != null){
            existingSection.setFloor(originalFloor);
            sectionRepository.save(existingSection);
            sectionRepository.flush();
        }

        SectionResponseDto sectionResponseDto = sectionMapper.toDto(section);
        sectionResponseDto.setFloorDetails(floorMapper.toDto(floor));
        return sectionResponseDto;
    }


    public SectionResponseDto changeAvailabilityOfSection(String sectionName, SectionAvailablityEnum sectionAvailablityEnum) throws Exception {
        Sections section = sectionRepository.findBySectionName(sectionName);

        if(section == null) throw new NotFoundException("No such Section Exist");

        section.setIsAvailable(sectionAvailablityEnum);
        sectionRepository.save(section);

        SectionResponseDto sectionResponseDto = sectionMapper.toDto((section));
        sectionResponseDto.setFloorDetails(floorMapper.toDto(section.getFloor()));

        return sectionResponseDto;
    }

    public void deleteSectionByName(String sectionName) throws Exception {
        List<Subjects> subjectsList = subjectsRepository.findBySectionId(getSectionByName(sectionName).getId());
         if(!subjectsList.isEmpty()){
            subjectsRepository.deleteAllById(subjectsList.stream().map(subjects -> {
                return subjects.getId();
            }).toList());
         }
         sectionRepository.deleteById(getSectionByName(sectionName).getId());
    }

    public SectionResponseDto addSection(SectionRequestDto sectionRequestDto) throws Exception {

        Sections section = sectionMapper.fromDto(sectionRequestDto);
        Floor floor = floorRepository.findByFloorNameAndWing(sectionRequestDto.getFloorName(), sectionRequestDto.getWing());

        if(floor == null) throw new NotFoundException("No such Floor Exist");

        section.setFloor(floor);
        sectionRepository.save(section);

        SectionResponseDto sectionResponseDto = sectionMapper.toDto(section);
        sectionResponseDto.setFloorDetails(floorMapper.toDto(floor));
        return sectionResponseDto;
    }

    public RecommendationResponseDto getRecommendationBySectionName(String sectionName) throws Exception {
        Floor floor = sectionRepository.findBySectionName(sectionName).getFloor();
        List<Subjects> subjectsList = subjectsRepository.findBySectionIdAndIsAvailable(getSectionByName(sectionName).getId(), SectionAvailablityEnum.AVAILABLE);

        StringBuilder subjectNames = new StringBuilder();

        for(int i = 0; i < subjectsList.size(); i++){
            subjectNames.append(subjectsList.get(i).getSubjectName());
            if(i + 1 != subjectsList.size())
                subjectNames.append(", ");
        }

        FloorRecommendationResponseDto floorRecomDto = floorMapper.toRecomDto(floor);
        ResourceAvailabilityDto resourcedto =  availabilityService.getDetails(floorRecomDto.getFloorName());
        floorRecomDto.setTotalTable(resourcedto.getTotalTable());
        floorRecomDto.setAvailableTable(resourcedto.getAvailableTable());
        floorRecomDto.setOccupiedTable(resourcedto.getOccupiedTable());
        floorRecomDto.setTotalCp(resourcedto.getTotalCp());
        floorRecomDto.setAvailableCp(resourcedto.getAvailableCp());
        floorRecomDto.setOccupiedCp(resourcedto.getOccupiedCp());
        return recommendationMapper.toDto(floorRecomDto, subjectNames.toString());
    }
}
