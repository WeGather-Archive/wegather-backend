package kr.wegather.wegather.domain;

import kr.wegather.wegather.domain.enums.ClubType;
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
public class Club {

    // Primary Key
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_idx")
    private Long id;


    // Foreign Keys - OneToMany
    @OneToMany(mappedBy = "club")
    private List<ClubMember> clubMembers = new ArrayList<>();

    @OneToMany(mappedBy = "club")
    private List<ClubRole> clubRoles = new ArrayList<>();

    @OneToMany(mappedBy = "club")
    private List<ClubSchool> clubSchools = new ArrayList<>();

    @OneToMany(mappedBy = "club")
    private List<Recruitment> recruitments = new ArrayList<>();


    // Foreign Keys - ManyToOne


    // Columns
    @Column(name = "club_name")
    private String name;

    private String club_avatar;

    private String introduction;

    @Enumerated(EnumType.ORDINAL)
    private ClubType type; // CLUB, STUDY

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
