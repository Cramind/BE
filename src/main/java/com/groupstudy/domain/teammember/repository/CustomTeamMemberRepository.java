package com.groupstudy.domain.teammember.repository;

import com.groupstudy.domain.team.entity.Team;
import com.groupstudy.domain.user.entity.User;

import java.util.List;

public interface CustomTeamMemberRepository {
    List<Long> findStudyRoomByUserId(Long userId);
    boolean existsByTeamAndUser(Team team, User user);
}
