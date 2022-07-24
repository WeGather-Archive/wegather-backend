package kr.wegather.wegather.domain;

import kr.wegather.wegather.domain.enums.ApplicantStatus;
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
public class Applicant {

    // Primary Key
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applicant_idx")
    private Long id;


    // Foreign Keys - OneToMany
    @OneToMany(mappedBy = "applicant")
    private List<Application> applications = new ArrayList<>();


    // Foreign Keys - ManyToOne
    @ManyToOne
    @JoinColumn(name = "recruitment_idx")
    private Recruitment recruitment;

    @ManyToOne
    @JoinColumn(name = "selection_idx")
    private Selection selection;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User user;


    // Columns
    @Enumerated(EnumType.ORDINAL)
    private ApplicantStatus status; // PENDING, APPROVED, FAILED

    @Column(name = "created_time")
    private Timestamp created;
}
