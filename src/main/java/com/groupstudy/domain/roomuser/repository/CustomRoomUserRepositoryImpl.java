package com.groupstudy.domain.roomuser.repository;

import com.groupstudy.domain.roomuser.entity.QRoomUser;
import com.groupstudy.domain.user.repository.CustomUserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomRoomUserRepositoryImpl implements CustomRoomUserRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> findStudyRoomByUserId(Long userId) {
        QRoomUser qRoomUser = QRoomUser.roomUser;

        return queryFactory
                .select(qRoomUser.parentTeam.id)
                .from(qRoomUser)
                .where(qRoomUser.id.eq(userId))
                .fetch();
    }
}
