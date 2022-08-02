package kr.wegather.wegather.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter @Setter
public class Selection {

    // Primary Key
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "selection_idx")
    private Long id;


    // Foreign Keys - OneToMany
    @JsonIgnore
    @OneToMany(mappedBy = "selection")
    private List<Applicant> applicants = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "selection")
    private List<Questionnaire> questionnaires = new ArrayList<>();


    // Foreign Keys - ManyToOne
    @ManyToOne
    @JoinColumn(name = "recruitment_idx")
    private Recruitment recruitment;


    // Columns
    private Integer order;

    @Column(name = "selection_name")
    private String name;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    private String location;

    @Column(name = "online_link")
    private String onlineLink;

    @Column(name = "is_online")
    private Boolean isOnline;
}
