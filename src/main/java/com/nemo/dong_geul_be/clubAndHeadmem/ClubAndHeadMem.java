package com.nemo.dong_geul_be.clubAndHeadmem;

import com.nemo.dong_geul_be.mypage.domain.MyClub;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
public class ClubAndHeadMem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clubName;
    private String managerEmail;

    @OneToMany(mappedBy = "clubAndHeadMem")
    private List<MyClub> clubMembers= new ArrayList<>();

}
