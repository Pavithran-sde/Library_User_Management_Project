package com.LibraryManagement.LibraryUserManagement.Admin.Entities;

import com.LibraryManagement.LibraryUserManagement.User.Enum.SectionAvailablityEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subjects {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String subjectName;

    @ManyToOne
    @JoinColumn(nullable = false, name = "section_id")
    private Sections section;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SectionAvailablityEnum isAvailable;
}
