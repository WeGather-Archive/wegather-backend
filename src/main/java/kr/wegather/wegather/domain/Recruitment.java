package kr.wegather.wegather.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.wegather.wegather.domain.enums.RecruitmentStatus;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_role_idx")
    private ClubRole clubRole;


    // Columns
    private String title;
    private String description;

    @Column(name = "created_time")
    private Timestamp created;

    @Enumerated(EnumType.ORDINAL)
    private RecruitmentStatus status; // CREATED, ENROLL, PROCESSING, CLOSED

    public JSONObject toJSONObjectForClub() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("clubRole", clubRole.toJSONObject());
            json.put("title", title);
            json.put("description", description);
            json.put("created", created);
            json.put("status", status);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            JSONArray selectionArray = new JSONArray();
            for (Selection selection: selections) {
                selectionArray.put(selection.getId());
            }
            json.put("selections", selectionArray);
            json.put("clubRole", clubRole.toJSONObject());
            json.put("title", title);
            json.put("description", description);
            json.put("created", created);
            json.put("status", status);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}
