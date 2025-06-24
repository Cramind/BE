package com.groupstudy.domain.user.repository;

import com.groupstudy.domain.user.entity.QUser;
import com.groupstudy.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<User> findByEmails(List<String> emails) {
        QUser qUser = QUser.user;
        return queryFactory.selectFrom(qUser)
                .where(qUser.email.in(emails))
                .fetch();
    }

    @Override
    public User findByEmail(String email) {
        QUser qUser = QUser.user;
        return queryFactory.selectFrom(qUser)
                .where(qUser.email.eq(email))
                .fetchOne();
    }
}
