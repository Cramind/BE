package com.groupstudy.domain.roomuser.entity;

import com.groupstudy.domain.team.entity.Team;
import com.groupstudy.domain.user.entity.User;
import com.groupstudy.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "room_user")
@Getter
@Builder
public class RoomUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team parentTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
