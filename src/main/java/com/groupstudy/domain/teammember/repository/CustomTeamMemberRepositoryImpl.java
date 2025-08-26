package com.groupstudy.domain.teammember.repository;

import com.groupstudy.domain.teammember.entity.QTeamMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomTeamMemberRepositoryImpl implements CustomTeamMemberRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> findStudyRoomByUserId(Long userId) {
        QTeamMember qTeamMember = QTeamMember.teamMember;

        return queryFactory
                .select(qTeamMember.parentTeam.id)
                .from(qTeamMember)
                .where(qTeamMember.id.eq(userId))
                .fetch();
    }
}
