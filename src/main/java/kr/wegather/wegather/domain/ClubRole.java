package kr.wegather.wegather.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
    @OneToMany(mappedBy = "clubRole")
    private List<ClubMemberRole> clubMemberRoles = new ArrayList<>();

    @OneToMany(mappedBy = "clubRole")
    private List<Recruitment> recruitments = new ArrayList<>();


    // Foreign Keys - ManyToOne
    @ManyToOne
    @JoinColumn(name = "club_idx")
    private Club club;


    // Columns
    private String role;
}
