package com.groupstudy.domain.team.repository;

import com.groupstudy.domain.team.entity.InviteCode;
import com.groupstudy.domain.user.entity.User;

import java.util.Optional;

public interface CustomInviteCodeRepository {
    String findByStudyRoomId(Long studyroomId);
    Long findStudyRoomByCode(String code);
    Optional<InviteCode> findByCode(String code);

}
