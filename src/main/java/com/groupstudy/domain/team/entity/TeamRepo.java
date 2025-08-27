package com.groupstudy.domain.team.entity;

import com.groupstudy.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team_repo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamRepo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_org_id")
    private TeamOrganization parentTeamOrg;

    private String repoName;


    public void assignTeamOrg(TeamOrganization teamOrg){
        this.parentTeamOrg = teamOrg;
    }
}
