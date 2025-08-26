package com.groupstudy.domain.team.service;

import com.groupstudy.domain.team.dto.TeamCreateDto;
import com.groupstudy.global.auth.CustomUserDetails;

public interface TeamCommandService {
    void insertNewTeam(CustomUserDetails customUserDetails, TeamCreateDto creationDto);
}
