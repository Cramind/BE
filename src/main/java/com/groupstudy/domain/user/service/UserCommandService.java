package com.groupstudy.domain.user.service;

import com.groupstudy.domain.auth.dto.request.SignUpRequest;
import com.groupstudy.domain.user.entity.User;
import com.groupstudy.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public void insertUser(SignUpRequest signUpRequest){
        userRepository.save(signUpRequest.toUser(encoder.encode(signUpRequest.password())));
    }

}
