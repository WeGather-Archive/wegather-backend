package kr.wegather.wegather.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
    @ManyToOne
    @JoinColumn(name = "club_idx")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "school_idx")
    private School school;


    // Columns
}
