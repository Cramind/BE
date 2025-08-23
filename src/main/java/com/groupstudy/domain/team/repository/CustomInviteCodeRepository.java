package com.groupstudy.domain.team.repository;

public interface CustomInviteCodeRepository {
    String findByStudyRoomId(Long studyroomId);
    Long findStudyRoomByCode(String code);
}
