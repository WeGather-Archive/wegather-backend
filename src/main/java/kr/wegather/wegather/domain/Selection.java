package kr.wegather.wegather.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.json.JSONArray;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_idx")
    private Recruitment recruitment;


    // Columns
    @Column(name = "selection_order")
    private Integer order;

    @Column(name = "selection_name")
    private String name;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    private String location;

    @Column(name = "online_link")
    private String onlineLink;

    @Column(name = "is_online")
    private Boolean isOnline;

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("order", order);
            json.put("name", name);
            json.put("start_time", startTime);
            json.put("end_time", endTime);
            json.put("location", location);
            json.put("online_link", onlineLink);
            json.put("is_online", isOnline);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    public JSONObject toJSONObjectForRecruitment() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            JSONArray applicantArray = new JSONArray();
            for (Applicant applicant: applicants) {
                applicantArray.put(applicant.toJSONObjectForRecruitment());
            }
            json.put("applicants", applicantArray);
            json.put("order", order);
            json.put("name", name);
            json.put("start_time", startTime);
            json.put("end_time", endTime);
            json.put("location", location);
            json.put("online_link", onlineLink);
            json.put("is_online", isOnline);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}
