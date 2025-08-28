package com.groupstudy.domain.team.controller;

import com.groupstudy.domain.team.dto.TeamCreateDto;
import com.groupstudy.domain.team.dto.response.TeamInvitationInfoResponse;
import com.groupstudy.domain.team.service.TeamCommandService;
import com.groupstudy.global.auth.CustomUserDetails;
import com.groupstudy.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamCommandService teamCommandService;

    @GetMapping("/info/{inviteCode}")
    public ResponseEntity<ApiResponse<TeamInvitationInfoResponse>> getTeamInvitationInfo(
            @PathVariable("inviteCode") String inviteCode
    ) {
        return ResponseEntity.ok(ApiResponse.onSuccess(teamCommandService.fetchTeamMetaData(inviteCode)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> postNewTeam(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody TeamCreateDto teamCreateDto){
        teamCommandService.insertNewTeam(customUserDetails, teamCreateDto);
        return ResponseEntity.ok(ApiResponse.onSuccessVoid());
    }

}
