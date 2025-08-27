package com.groupstudy.domain.team.entity;

import com.groupstudy.domain.teammember.entity.TeamMember;
import com.groupstudy.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "team")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "team_id")
    private Long id;

    private String teamName;
    private String teamDescription;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMember> teamUsers;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamOrganization> teamOrganizations;

    public Team(String teamName, String teamDescription){
        this.teamName = teamName;
        this.teamDescription = teamDescription;
    }

}
