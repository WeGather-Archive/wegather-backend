package kr.wegather.wegather.domain;

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
public class ClubMember {

    // Primary Key
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_member_idx")
    private Long id;


    // Foreign Keys - OneToMany
    @OneToMany(mappedBy = "clubMember")
    private List<ClubMemberRole> clubMemberRoles = new ArrayList<>();


    // Foreign Keys - ManyToOne
    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User user;

    @ManyToOne
    @JoinColumn(name = "club_idx")
    private Club club;


    // Columns
    @Column(name = "is_deleted")
    private Boolean isDeleted; // ACTIVATED, DELETED

    @Column(name = "joined_time")
    private Timestamp joinedTime;
}
