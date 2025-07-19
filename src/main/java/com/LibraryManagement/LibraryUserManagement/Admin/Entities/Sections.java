package com.LibraryManagement.LibraryUserManagement.Admin.Entities;

import com.LibraryManagement.LibraryUserManagement.User.Enum.SectionAvailablityEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "sections")
public class Sections {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String sectionName;

    @OneToOne
    @JoinColumn(nullable = false, name = "floor_id")
    private Floor floor;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SectionAvailablityEnum isAvailable;

    @JsonIgnore
    @OneToMany(mappedBy = "section")
    private List<Subjects> subjectsList;



}
