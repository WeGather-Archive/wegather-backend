package kr.wegather.wegather.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.wegather.wegather.StringListConverter;
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
public class Application {

    // Primary Key
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_idx")
    private Long id;

    // Foreign Keys - OneToMany


    // Foreign Keys - ManyToOne
    @ManyToOne
    @JoinColumn(name = "applicant_idx")
    private Applicant applicant;

    @ManyToOne
    @JoinColumn(name = "questionnaire_idx")
    private Questionnaire questionnaire;


    // Columns
    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "json")
    private ArrayList<String> question;

    @Column(name = "created_time")
    private Timestamp created;
}
