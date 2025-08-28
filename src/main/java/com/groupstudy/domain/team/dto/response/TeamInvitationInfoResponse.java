package com.groupstudy.domain.team.dto.response;

import java.util.List;

public record TeamInvitationInfoResponse(
        String teamName,
        String teamDescription,
        Integer teamMemberCnt,
        Integer activeMemberCnt,
        String invitor,
        List<String> teamMembers) {
}
