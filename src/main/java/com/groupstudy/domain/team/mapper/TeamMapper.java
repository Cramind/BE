package com.groupstudy.domain.team.mapper;

import com.groupstudy.domain.team.dto.TeamCreateDto;
import com.groupstudy.domain.team.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TeamMapper {

    Team toTeam(TeamCreateDto teamCreateDto);

}
