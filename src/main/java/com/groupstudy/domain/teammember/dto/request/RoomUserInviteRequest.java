package com.groupstudy.domain.teammember.dto.request;

import com.groupstudy.domain.teammember.controller.validation.ExistingEmails;
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
