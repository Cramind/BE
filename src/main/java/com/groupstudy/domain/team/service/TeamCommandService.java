package com.groupstudy.domain.team.service;

import com.groupstudy.domain.team.dto.TeamCreateDto;

public interface TeamCommandService {
    void insertNewTeam(TeamCreateDto creationDto);
}
