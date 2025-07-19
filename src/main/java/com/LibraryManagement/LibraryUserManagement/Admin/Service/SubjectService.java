package com.LibraryManagement.LibraryUserManagement.Admin.Service;

import com.LibraryManagement.LibraryUserManagement.Admin.DTO.Recommendation.RecommendationResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.SectionDto.SectionResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.Subjects.SubjectRequestDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.Subjects.SubjectResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Sections;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Subjects;
import com.LibraryManagement.LibraryUserManagement.Admin.Mappers.FloorMapper;
import com.LibraryManagement.LibraryUserManagement.Admin.Mappers.SectionMapper;
import com.LibraryManagement.LibraryUserManagement.Admin.Mappers.SubjectMapper;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.SectionRepository;
import com.LibraryManagement.LibraryUserManagement.Admin.Repository.SubjectsRepository;
import com.LibraryManagement.LibraryUserManagement.User.Enum.SectionAvailablityEnum;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectsRepository subjectsRepository;
    @Autowired
    private SubjectMapper subjectMapper;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private SectionMapper sectionMapper;
    @Autowired
    private FloorMapper floorMapper;
    @Autowired
    private SectionService sectionService;

    public List<SubjectResponseDto> getAllSubjects() throws Exception {

        List<Subjects> subjectsList = subjectsRepository.findAllByOrderBySectionAscSubjectNameAsc();
        if (subjectsList.isEmpty()){
            throw new NoContentFoundException("No Subject Found");
        }

        return getSubjectDetails(subjectsList);
    }

    public List<SubjectResponseDto> getAllAvailableSubjects() throws Exception{
       List<Subjects> subjectsList = subjectsRepository.findByIsAvailable(SectionAvailablityEnum.AVAILABLE);
       if(subjectsList.isEmpty()) throw new NoContentFoundException("No Subject is Available");
       return getSubjectDetails(subjectsList);
    }


    private List<SubjectResponseDto> getSubjectDetails(List<Subjects> subjectsList){
        return subjectsList.stream().map(subjects -> {
                SubjectResponseDto subjectResponseDto = subjectMapper.toDto(subjects);

            SectionResponseDto sectionResponseDto = sectionMapper.toDto(subjects.getSection());
            sectionResponseDto.setFloorDetails(floorMapper.toDto(subjects.getSection().getFloor()));
            subjectResponseDto.setSectionDetails(sectionResponseDto);
            return  subjectResponseDto;
        }).toList();
    }

    public SubjectResponseDto getSubjectByName(String subjectName) throws Exception {
        Subjects subject = subjectsRepository.findBySubjectName(subjectName);
        if(subject == null){
            throw new NotFoundException("No Such Subject Exist");
        }
        SubjectResponseDto subjectResponseDto = subjectMapper.toDto(subject);
        SectionResponseDto sectionResponseDto = sectionMapper.toDto(subject.getSection());
        sectionResponseDto.setFloorDetails(floorMapper.toDto(subject.getSection().getFloor()));
        subjectResponseDto.setSectionDetails(sectionResponseDto);


        return subjectResponseDto;
    }


    public SubjectResponseDto addSubject(@Valid SubjectRequestDto subjectRequestDto) throws Exception {
        Subjects subject = subjectMapper.fromDto(subjectRequestDto);

        Sections section = sectionRepository.findBySectionName(subjectRequestDto.getSectionName());
        if(section == null) throw new NotFoundException("No such Section Found");

        subject.setSection(section);
        subjectsRepository.save(subject);

        SubjectResponseDto subjectResponseDto = subjectMapper.toDto(subject);
        SectionResponseDto sectionResponseDto = sectionMapper.toDto(section);
        sectionResponseDto.setFloorDetails(floorMapper.toDto(section.getFloor()));
        subjectResponseDto.setSectionDetails(sectionResponseDto);

        return subjectResponseDto;
    }

    public SubjectResponseDto updateSubjectBySubjectName(SubjectRequestDto subjectRequestDto) throws Exception {

        Subjects existingSubject = subjectsRepository.findBySubjectName(subjectRequestDto.getSubjectName());
        if(existingSubject == null) throw new NotFoundException("No Such Subject Exist");

        Sections section = sectionRepository.findBySectionName(subjectRequestDto.getSectionName());
        if(section == null) throw new NotFoundException("No Such Section Exist");

        existingSubject.setIsAvailable(subjectRequestDto.getIsAvailable());
        existingSubject.setSection(section);
        subjectsRepository.save(existingSubject);

        SubjectResponseDto subjectResponseDto = subjectMapper.toDto(existingSubject);
        SectionResponseDto sectionResponseDto = sectionMapper.toDto(section);
        sectionResponseDto.setFloorDetails(floorMapper.toDto(section.getFloor()));
        subjectResponseDto.setSectionDetails(sectionResponseDto);

        return subjectResponseDto;
    }



    public void deleteSubjectBySubjectName(String subjectName) throws Exception {
           subjectsRepository.deleteById(getSubjectByName(subjectName).getId());
    }

    public RecommendationResponseDto getRecommendationBySubjectName(String subjectName) throws Exception {

        Subjects subject = subjectsRepository.findBySubjectName(subjectName);

        return sectionService.getRecommendationBySectionName(subject.getSection().getSectionName());
    }
}
