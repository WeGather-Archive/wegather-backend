package kr.wegather.wegather.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.wegather.wegather.domain.enums.ApplicantStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.json.JSONException;
import org.json.JSONObject;

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
    @JsonIgnore
    @OneToMany(mappedBy = "applicant")
    private List<Application> applications = new ArrayList<>();


    // Foreign Keys - ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_idx")
    private Recruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selection_idx")
    private Selection selection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;


    // Columns
    @Enumerated(EnumType.ORDINAL)
    private ApplicantStatus status; // PENDING, APPROVED, FAILED

    @Column(name = "created_time")
    private Timestamp created;

    public JSONObject toJSONObjectForRecruitment() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("status", status);
            json.put("created", created);
            json.put("user", user.toJSONObjet());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    public JSONObject toJSONObjectForClub() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("selection", selection.toJSONObject());
            json.put("status", status);
            json.put("created", created);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}
