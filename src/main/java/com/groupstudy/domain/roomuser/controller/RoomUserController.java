package com.groupstudy.domain.roomuser.controller;

import com.groupstudy.domain.roomuser.dto.response.StudyRoomListResponse;
import com.groupstudy.domain.roomuser.service.RoomUserCommandService;
import com.groupstudy.domain.roomuser.service.RoomUserQueryService;
import com.groupstudy.global.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roomusers")
@CrossOrigin(value = "http://localhost:5173")
public class RoomUserController {
    private final RoomUserCommandService roomUserCommandService;
    private final RoomUserQueryService roomUserQueryService;


    @PostMapping("/{roomId}/code")
    public String getOrCreateInviteCode(@PathVariable Long roomId){
        return roomUserCommandService.getOrCreateInviteCode(roomId);
    }

    @PostMapping("/{code}")
    public void inviteUserToRoom(@PathVariable String code,
                                 @AuthenticationPrincipal CustomUserDetails customUserDetails){
        roomUserCommandService.insertRoomUser(code, customUserDetails);
    }

//    @GetMapping
//    public StudyRoomListResponse getStudyRoomParticipatedIn(
//            @AuthenticationPrincipal CustomUserDetails customUserDetails){
//        return roomUserQueryService.selectParticipatedStudyRooms(customUserDetails);
//    }
}
