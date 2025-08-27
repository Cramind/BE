package com.groupstudy.domain.team.entity;

import com.groupstudy.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "team_organization")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamOrganization extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "team_org_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team parentTeam;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentTeamOrg", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamRepo> childrenRepos;

    private String orgName;

    public TeamOrganization(String orgName) {
        this.orgName = orgName;
    }

    public void assignTeam(Team team){
        this.parentTeam = team;
    }
}
