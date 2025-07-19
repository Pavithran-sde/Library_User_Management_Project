package com.LibraryManagement.LibraryUserManagement.Admin.Repository;

import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Sections;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Sections, Long> {

    Sections findBySectionName(String section);

    Sections findByFloorId(long id);
}
