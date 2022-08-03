package kr.wegather.wegather.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.wegather.wegather.domain.enums.ClubRoleAuthLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter @Setter
public class ClubRole {

    // Primary Key
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_role_idx")
    private Long id;


    // Foreign Keys - OneToMany
    @JsonIgnore
    @OneToMany(mappedBy = "clubRole")
    private List<Recruitment> recruitments = new ArrayList<>();


    // Foreign Keys - ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_idx")
    private Club club;


    // Columns
    @Column
    private String role;

    @Column(name = "auth_level")
    @Enumerated(EnumType.ORDINAL)
    private ClubRoleAuthLevel authLevel; // OPERATOR, MEMBER

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("role", role);
            json.put("auth_level", authLevel);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}
