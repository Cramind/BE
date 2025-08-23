package com.groupstudy.domain.team.mapper;

import com.groupstudy.domain.team.dto.TeamCreateDto;
import com.groupstudy.domain.team.entity.Team;
import com.groupstudy.domain.team.entity.TeamOrganization;
import org.aspectj.lang.annotation.After;
import org.mapstruct.*;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TeamOrgMapper {

    @Mapping(target = "orgName", source = "teamCreateDto.selectedOrganization")
    TeamOrganization toTeamOrg(TeamCreateDto teamCreateDto, @Context Team team);

    @AfterMapping
    default void assignTeam(@MappingTarget TeamOrganization teamOrganization, @Context Team team){
        teamOrganization.assignTeam(team);
    }
}
