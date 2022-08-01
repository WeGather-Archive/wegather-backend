package kr.wegather.wegather.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class School {

    // Primary Key
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_idx")
    private Long id;


    // Foreign Keys - OneToMany
    @JsonIgnore
    @OneToMany(mappedBy = "school")
    private List<ClubSchool> clubSchools = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "school")
    private List<SchoolDept> schoolDepts = new ArrayList<>();


    // Foreign Keys - ManyToOne


    // Columns
    @Column(name = "school_name")
    private String name;

    private String location;

    private String branch;

    @Column(name = "school_gubun")
    private String gubun;
}
