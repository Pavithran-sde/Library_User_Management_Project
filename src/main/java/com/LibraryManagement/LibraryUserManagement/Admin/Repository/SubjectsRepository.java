package com.LibraryManagement.LibraryUserManagement.Admin.Repository;

import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Subjects;
import com.LibraryManagement.LibraryUserManagement.User.Enum.SectionAvailablityEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectsRepository extends JpaRepository<Subjects, Long> {

        Subjects findBySubjectName(String subject);

        List<Subjects>  findByIsAvailable(SectionAvailablityEnum status);

        List<Subjects> findAllByOrderBySectionAscSubjectNameAsc();

       // List<Subjects> findBySubjectIdContainingIgnoreCase(long  id);

        List<Subjects> findBySectionId(long id);

        List<Subjects> findBySectionIdAndIsAvailable(long id, SectionAvailablityEnum status);

}
