package com.groupstudy.domain.roomuser.repository;

import java.util.List;

public interface CustomRoomUserRepository {
    List<Long> findStudyRoomByUserId(Long userId);
}
