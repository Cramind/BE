package com.groupstudy.domain.auth.controller;

import com.groupstudy.domain.auth.dto.request.LoginRequest;
import com.groupstudy.domain.auth.dto.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:5173")
public class AuthController {

    @PostMapping("/login")
    public void doLogin(@RequestBody LoginRequest loginRequest){

    }

}
