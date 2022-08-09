package kr.wegather.wegather.domain;

import kr.wegather.wegather.StringListConverter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.json.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter @Setter
@Table(
        uniqueConstraints={
                @UniqueConstraint(
                        name="applicant_idx_questionnaire_idx",
                        columnNames={"applicant_idx", "questionnaire_idx"}
                )
        }
)
public class Application {

    // Primary Key
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_idx")
    private Long id;

    // Foreign Keys - OneToMany


    // Foreign Keys - ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_idx")
    private Applicant applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionnaire_idx")
    private Questionnaire questionnaire;


    // Columns
    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "json")
    private List<String> answer;

    @Column(name = "created_time")
    private Timestamp created;

    @Column(name = "last_modified")
    private Timestamp lastModified;
}
