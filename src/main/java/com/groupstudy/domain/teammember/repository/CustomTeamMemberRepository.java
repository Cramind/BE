package com.groupstudy.domain.teammember.repository;

import java.util.List;

public interface CustomTeamMemberRepository {
    List<Long> findStudyRoomByUserId(Long userId);
}
