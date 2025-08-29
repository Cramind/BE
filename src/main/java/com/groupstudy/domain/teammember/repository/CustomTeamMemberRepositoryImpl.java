package com.groupstudy.domain.teammember.repository;

import com.groupstudy.domain.team.entity.Team;
import com.groupstudy.domain.teammember.entity.QTeamMember;
import com.groupstudy.domain.user.entity.User;
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

    @Override
    public boolean existsByTeamAndUser(Team team, User user) {
        QTeamMember qTeamMember = QTeamMember.teamMember;

        return queryFactory.selectOne()
                .from(qTeamMember)
                .where(qTeamMember.parentTeam.eq(team), qTeamMember.user.eq(user))
                .fetchFirst() != null;
    }
}
