package kr.wegather.wegather.domain;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Getter @Setter
public class ClubSchool {

    // Primary Key
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_school_idx")
    private Long id;

    // Foreign Keys - OneToMany


    // Foreign Keys - ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_idx")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_idx")
    private School school;


    // Columns


    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("club", club);
            json.put("school", school);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}
