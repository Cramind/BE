package com.groupstudy.domain.roomuser.controller;

import com.groupstudy.domain.roomuser.dto.request.RoomUserInviteRequest;
import com.groupstudy.domain.roomuser.service.RoomUserCommandService;
import com.groupstudy.global.auth.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roomusers")
@CrossOrigin(value = "http://localhost:5173")
public class RoomUserController {
    private final RoomUserCommandService roomUserCommandService;


    @PostMapping("/{roomId}/code")
    public String getOrCreateInviteCode(@PathVariable Long roomId){
        return roomUserCommandService.getOrCreateInviteCode(roomId);
    }

    @PostMapping("/{code}")
    public void inviteUserToRoom(@PathVariable String code,
                                 @AuthenticationPrincipal CustomUserDetails customUserDetails){
        roomUserCommandService.insertRoomUser(code, customUserDetails);
    }
}
