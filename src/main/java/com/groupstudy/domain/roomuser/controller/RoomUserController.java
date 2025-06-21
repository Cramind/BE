package com.groupstudy.domain.roomuser.controller;

import com.groupstudy.domain.roomuser.dto.request.RoomUserInviteRequest;
import com.groupstudy.domain.roomuser.service.RoomUserCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roomusers")
public class RoomUserController {
    private final RoomUserCommandService roomUserCommandService;

    @PostMapping("/{roomId}")
    public void inviteUserToRoom(@PathVariable Long roomId,
                                 @Valid @RequestBody RoomUserInviteRequest request){
        roomUserCommandService.insertRoomUser(roomId, request);
    }
}
