package com.groupstudy.domain.studyroom.repository;

public interface CustomInviteCodeRepository {
    String findByStudyRoomId(Long studyroomId);
    Long findStudyRoomByCode(String code);
}
