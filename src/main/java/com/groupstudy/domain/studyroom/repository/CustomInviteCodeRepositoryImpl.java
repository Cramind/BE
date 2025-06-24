package com.groupstudy.domain.studyroom.repository;

import com.groupstudy.domain.studyroom.entity.QInviteCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomInviteCodeRepositoryImpl implements CustomInviteCodeRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public String findByStudyRoomId(Long studyroomId) {
        QInviteCode qInviteCode = QInviteCode.inviteCode;
        return queryFactory
                .select(qInviteCode.code)
                .from(qInviteCode)
                .where(qInviteCode.studyRoom.id.eq(studyroomId))
                .fetchFirst();
    }

    @Override
    public Long findStudyRoomByCode(String code) {
        QInviteCode qInviteCode = QInviteCode.inviteCode;
        return queryFactory.select(qInviteCode.studyRoom.id)
                .from(qInviteCode)
                .where(qInviteCode.code.eq(code))
                .fetchOne();
    }
}
