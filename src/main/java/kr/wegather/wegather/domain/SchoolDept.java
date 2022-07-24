package kr.wegather.wegather.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class SchoolDept {

    // Primary Key
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_dept_idx")
    private Long id;


    // Foreign Keys - OneToMany
    @OneToMany(mappedBy = "schoolDept")
    private List<User> users = new ArrayList<>();


    // Foreign Keys - ManyToOne
    @ManyToOne
    @JoinColumn(name = "school_idx")
    private School school;


    // Columns
    private String college;
    private String dept;

}
