package com.groupstudy.domain.team.repository;

import com.groupstudy.domain.team.entity.InviteCode;
import com.groupstudy.domain.team.entity.QInviteCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
                .where(qInviteCode.team.id.eq(studyroomId))
                .fetchFirst();
    }

    @Override
    public Long findStudyRoomByCode(String code) {
        QInviteCode qInviteCode = QInviteCode.inviteCode;
        return queryFactory.select(qInviteCode.team.id)
                .from(qInviteCode)
                .where(qInviteCode.code.eq(code))
                .fetchOne();
    }

    @Override
    public Optional<InviteCode> findByCode(String code) {
        QInviteCode qInviteCode = QInviteCode.inviteCode;
        return Optional.ofNullable(queryFactory.selectFrom(qInviteCode)
                .where(qInviteCode.code.eq(code))
                .fetchOne());
    }
}
