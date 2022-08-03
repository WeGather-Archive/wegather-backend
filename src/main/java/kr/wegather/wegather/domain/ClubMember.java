package kr.wegather.wegather.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter @Setter
@Table(
        uniqueConstraints={
                @UniqueConstraint(
                        name="user_idx_club_idx",
                        columnNames={"user_idx", "club_idx"}
                )
        }
)
public class ClubMember {

    // Primary Key
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_member_idx")
    private Long id;


    // Foreign Keys - OneToMany


    // Foreign Keys - ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_idx")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_role_idx")
    private ClubRole role;


    // Columns
    @Column(name = "is_deleted")
    private Boolean isDeleted; // ACTIVATED, DELETED

    @Column(name = "joined_time")
    private Timestamp joinedTime;


    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("user", user.toJSONObjectForClub());
            json.put("club", club.getId());
            json.put("joined_time", joinedTime);
            json.put("club_role", role.toJSONObject());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}
