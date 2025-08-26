package com.groupstudy.domain.team.service;

import com.groupstudy.domain.team.dto.TeamCreateDto;
import com.groupstudy.domain.team.entity.Team;
import com.groupstudy.domain.team.entity.TeamOrganization;
import com.groupstudy.domain.team.mapper.TeamMapper;
import com.groupstudy.domain.team.mapper.TeamOrgMapper;
import com.groupstudy.domain.team.repository.TeamOrgRepository;
import com.groupstudy.domain.team.repository.TeamRepository;
import com.groupstudy.domain.teammember.entity.TeamMember;
import com.groupstudy.domain.teammember.repository.TeamMemberRepository;
import com.groupstudy.domain.user.entity.User;
import com.groupstudy.global.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamCommandServiceImpl implements TeamCommandService{
    private final TeamRepository teamRepository;
    private final TeamOrgRepository teamOrgRepository;
    private final TeamMemberRepository teamMemberRepository;

    private final TeamMapper teamMapper;
    private final TeamOrgMapper orgMapper;

    @Override
    public void insertNewTeam(CustomUserDetails customUserDetails, TeamCreateDto creationDto) {
        Team team = teamMapper.toTeam(creationDto);
        teamRepository.save(team);

        User user = customUserDetails.getUser();

        TeamMember teamMember = new TeamMember(team, user);
        teamMemberRepository.save(teamMember);

        if (creationDto.selectedOrganization() != null && !creationDto.selectedOrganization().isEmpty()){
            TeamOrganization teamOrganization = orgMapper.toTeamOrg(creationDto, team);
            teamOrgRepository.save(teamOrganization);
        }
    }
}
