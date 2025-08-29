package com.groupstudy.domain.teammember.controller;

import com.groupstudy.domain.teammember.service.TeamMemberCommandService;
import com.groupstudy.domain.teammember.service.TeamMemberQueryService;
import com.groupstudy.global.auth.CustomUserDetails;
import com.groupstudy.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teammembers")
@CrossOrigin(value = "http://localhost:5173")
public class TeamMemberController {
    private final TeamMemberCommandService teamMemberCommandService;
    private final TeamMemberQueryService teamMemberQueryService;

    @GetMapping("/{roomId}/code")
    public ResponseEntity<ApiResponse<String>> getOrCreateInviteCode(@PathVariable Long roomId){
        return ResponseEntity.ok(ApiResponse.onSuccess(teamMemberCommandService.getOrCreateInviteCode(roomId)));
    }

    @PostMapping("/{code}")
    public ResponseEntity<ApiResponse<Long>> inviteUserToRoom(@PathVariable String code,
                                 @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(ApiResponse.onSuccess(teamMemberCommandService.insertRoomUser(code, customUserDetails)));
    }

//    @GetMapping
//    public StudyRoomListResponse getStudyRoomParticipatedIn(
//            @AuthenticationPrincipal CustomUserDetails customUserDetails){
//        return roomUserQueryService.selectParticipatedStudyRooms(customUserDetails);
//    }
}
