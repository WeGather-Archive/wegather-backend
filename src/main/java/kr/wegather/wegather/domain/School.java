package kr.wegather.wegather.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class School {

    // Primary Key
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_idx")
    private Long id;


    // Foreign Keys - OneToMany
    @JsonIgnore
    @OneToMany(mappedBy = "school")
    private List<ClubSchool> clubSchools = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "school")
    private List<SchoolDept> schoolDepts = new ArrayList<>();


    // Foreign Keys - ManyToOne


    // Columns
    @Column(name = "school_name")
    private String name;

    private String location;

    private String branch;

    @Column(name = "school_gubun")
    private String gubun;

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("name", name);
            json.put("location", location);
            json.put("branch", branch);
            json.put("gubun", gubun);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}
