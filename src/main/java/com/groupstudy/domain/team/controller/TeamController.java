package com.groupstudy.domain.team.controller;

import com.groupstudy.domain.team.dto.TeamCreateDto;
import com.groupstudy.domain.team.service.TeamCommandService;
import com.groupstudy.global.auth.CustomUserDetails;
import com.groupstudy.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamCommandService teamCommandService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> postNewTeam(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody TeamCreateDto teamCreateDto){
        teamCommandService.insertNewTeam(customUserDetails, teamCreateDto);
        return ResponseEntity.ok(ApiResponse.onSuccessVoid());
    }

}
