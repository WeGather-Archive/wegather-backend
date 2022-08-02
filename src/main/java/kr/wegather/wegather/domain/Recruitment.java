package kr.wegather.wegather.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.wegather.wegather.domain.enums.RecruitmentStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter @Setter
public class Recruitment {

    // Primary Key
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruitment_idx")
    private Long id;


    // Foreign Keys - OneToMany
    @JsonIgnore
    @OneToMany(mappedBy = "recruitment")
    private List<Applicant> applicants = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "recruitment")
    private List<Selection> selections = new ArrayList<>();


    // Foreign Keys - ManyToOne
    @ManyToOne
    @JoinColumn(name = "club_idx")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "club_role_idx")
    private ClubRole clubRole;


    // Columns
    private String title;
    private String description;

    @Column(name = "created_time")
    private Timestamp created;

    @Enumerated(EnumType.ORDINAL)
    private RecruitmentStatus status; // CREATED, ENROLL, PROCESSING, CLOSED
}
