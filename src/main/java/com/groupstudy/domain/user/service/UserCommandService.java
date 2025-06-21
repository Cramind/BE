package com.groupstudy.domain.user.service;

import com.groupstudy.domain.user.entity.User;
import com.groupstudy.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandService {
    private final UserRepository userRepository;

}
