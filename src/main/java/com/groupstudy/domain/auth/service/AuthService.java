package com.groupstudy.domain.auth.service;

import com.groupstudy.domain.user.entity.User;
import com.groupstudy.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    @Transactional
    public void onLoginSuccess(Long userId){
        User user = userRepository.findById(userId).orElseThrow();
        user.login();
    }

}
