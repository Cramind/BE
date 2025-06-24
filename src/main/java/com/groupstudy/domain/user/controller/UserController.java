package com.groupstudy.domain.user.controller;

import com.groupstudy.domain.auth.dto.request.SignUpRequest;
import com.groupstudy.domain.user.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserCommandService userCommandService;

    @PostMapping("/signup")
    public void signUpUser(@RequestBody SignUpRequest signUpRequest){
        userCommandService.insertUser(signUpRequest);
    }
}
