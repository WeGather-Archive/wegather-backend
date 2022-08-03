package kr.wegather.wegather.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.wegather.wegather.domain.enums.ClubType;
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
public class Club {

    // Primary Key
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_idx")
    private Long id;


    // Foreign Keys - OneToMany
    @JsonIgnore
    @OneToMany(mappedBy = "club")
    private List<ClubMember> clubMembers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "club")
    private List<ClubRole> clubRoles = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "club")
    private List<ClubSchool> clubSchools = new ArrayList<>();


    // Foreign Keys - ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_admin_idx")
    private User admin;


    // Column
    @Column(name = "club_phone")
    private String phone;

    // Columns
    @Column(name = "club_name")
    private String name;

    @Column(name = "club_avatar")
    private String avatar;

    private String introduction;

    @Enumerated(EnumType.ORDINAL)
    private ClubType type; // CLUB, STUDY

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("admin", admin.toJSONObjectForClub());
            json.put("phone", phone);
            json.put("name", name);
            json.put("avatar", avatar);
            json.put("introduction", introduction);
            json.put("type", type);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}
