package kr.wegather.wegather.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.wegather.wegather.domain.enums.AuthLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
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


    // Foreign Keys - ManyToOne
    @ManyToOne
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
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String phone;

    @Column
    private String imgUrl;

    @Column(name = "created_time")
    private Timestamp createdTime;

    @Column(name = "auth_level")
    @Enumerated(EnumType.ORDINAL)
    private AuthLevel authLevel;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "is_verified")
    private Boolean isVerified;
}
