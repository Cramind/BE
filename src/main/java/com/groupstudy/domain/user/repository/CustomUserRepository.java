package com.groupstudy.domain.user.repository;

import com.groupstudy.domain.user.entity.User;

import java.util.List;

public interface CustomUserRepository {
    List<User> findByEmails(List<String> email);
    User findByEmail(String email);
}
