package kr.wegather.wegather.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    @JsonIgnore
    @OneToMany(mappedBy = "clubMember")
    private List<ClubMemberRole> clubMemberRoles = new ArrayList<>();


    // Foreign Keys - ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_idx")
    private Club club;


    // Columns
    @Column(name = "is_deleted")
    private Boolean isDeleted; // ACTIVATED, DELETED

    @Column(name = "joined_time")
    private Timestamp joinedTime;
}
