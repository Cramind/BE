package com.groupstudy.domain.team.entity;

import com.groupstudy.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team_organization")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamOrganization extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team parentTeam;

    private String orgName;

    public TeamOrganization(String orgName) {
        this.orgName = orgName;
    }

    public void assignTeam(Team team){
        this.parentTeam = team;
    }
}
