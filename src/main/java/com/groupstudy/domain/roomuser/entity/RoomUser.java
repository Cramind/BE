package com.groupstudy.domain.roomuser.entity;

import com.groupstudy.domain.studyroom.entity.StudyRoom;
import com.groupstudy.domain.user.entity.User;
import com.groupstudy.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "project_user")
@Getter
@Builder
public class RoomUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studyroom_id")
    private StudyRoom studyRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
