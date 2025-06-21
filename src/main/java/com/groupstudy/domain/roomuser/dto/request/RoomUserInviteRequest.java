package com.groupstudy.domain.roomuser.dto.request;

import com.groupstudy.domain.roomuser.controller.validation.ExistingEmails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomUserInviteRequest {

    @ExistingEmails
    private List<String> invitedUsers;
}
