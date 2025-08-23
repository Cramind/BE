package com.groupstudy.domain.team.entity;

import com.groupstudy.domain.roomuser.entity.RoomUser;
import com.groupstudy.domain.user.entity.User;
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
    private List<RoomUser> teamUsers;

    public Team(String teamName, String teamDescription){
        this.teamName = teamName;
        this.teamDescription = teamDescription;
    }

}
