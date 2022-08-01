package kr.wegather.wegather.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.wegather.wegather.StringListConverter;
import kr.wegather.wegather.domain.enums.QuestionnaireStatus;
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
public class Questionnaire {

    // Primary Key
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "questionnaire_idx")
    private Long id;


    // Foreign Keys - OneToMany
    @JsonIgnore
    @OneToMany(mappedBy = "questionnaire")
    private List<Application> applications = new ArrayList<>();


    // Foreign Keys - ManyToOne
    @ManyToOne
    @JoinColumn(name = "selection_idx")
    private Selection selection;


    // Columns
    private String title;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "json")
    private List<String> question;

    @Column(name = "created_time")
    private Timestamp created;

    @Column(name = "last_modified")
    private Timestamp lastModified;

    @Enumerated(EnumType.ORDINAL)
    private QuestionnaireStatus status; // CREATED, STARTED, CLOSED
}
