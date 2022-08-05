package kr.wegather.wegather.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import kr.wegather.wegather.domain.enums.AuthLevel;
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
public class User{

    // Primary Key
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long id;


    // Foreign Keys - OneToMany
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Applicant> applicants = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<ClubMember> clubMembers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "admin")
    private List<Club> clubs = new ArrayList<>();

    // Foreign Keys - ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_dept_idx")
    private SchoolDept schoolDept;


    // Columns
    @Column
    private String nickname;

    @Column(name = "user_avatar")
    private String avatar;

    @Column
    private String profile;

    @Column(nullable = false)
    @ApiModelProperty(example = "홍길동")
    private String name;

    @Column(nullable = false, unique = true)
    @ApiModelProperty(example = "test@test.com")
    private String email;

    @Column(nullable = false)
    @ApiModelProperty(example = "password")
    private String password;

    @Column
    private String phone;

    @Column(name = "created_time")
    private Timestamp createdTime;

    @Column(name = "auth_level")
    @Enumerated(EnumType.ORDINAL)
    private AuthLevel authLevel;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "is_verified")
    private Boolean isVerified;

    public JSONObject toJSONObjet() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("schoolDept", schoolDept.toJSONObject());
            json.put("nickname", nickname);
            json.put("avatar", avatar);
            json.put("profile", profile);
            json.put("name", name);
            json.put("email", email);
            json.put("phone", phone);
            json.put("create_time", createdTime);
            json.put("auth_level", authLevel);
            json.put("isVerified", isVerified);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    public JSONObject toJSONObjectForClub() {
        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}
