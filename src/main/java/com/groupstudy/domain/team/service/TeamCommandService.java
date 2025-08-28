package com.groupstudy.domain.team.service;

import com.groupstudy.domain.team.dto.TeamCreateDto;
import com.groupstudy.domain.team.dto.response.TeamInvitationInfoResponse;
import com.groupstudy.domain.teammember.dto.response.TeamMemberInfoResponse;
import com.groupstudy.global.auth.CustomUserDetails;

public interface TeamCommandService {
    void insertNewTeam(CustomUserDetails customUserDetails, TeamCreateDto creationDto);
    TeamInvitationInfoResponse fetchTeamMetaData(String inviteCode);
}
