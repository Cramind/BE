package com.groupstudy.domain.auth.dto.request;

import com.groupstudy.domain.user.entity.User;

public record SignUpRequest(
        String email,
        String password
) {
    public User toUser(String encodedPassword){
        return User.builder()
                .email(this.email)
                .password(encodedPassword)
                .build();
    }

}
