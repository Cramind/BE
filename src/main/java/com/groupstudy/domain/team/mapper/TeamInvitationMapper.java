package com.groupstudy.domain.team.mapper;

import com.groupstudy.domain.team.dto.response.TeamInvitationInfoResponse;
import com.groupstudy.domain.team.entity.Team;
import com.groupstudy.domain.team.entity.TeamOrganization;
import com.groupstudy.domain.teammember.entity.TeamMember;
import com.groupstudy.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TeamInvitationMapper {

    @Mapping(target = "teamName", source = "team.teamName")
    @Mapping(target = "teamDescription", source = "team.teamDescription")
    @Mapping(target = "invitor", source = "user.name")
    @Mapping(target = "teamMembers", source = "teamMembers")
    TeamInvitationInfoResponse toInvitationInfoResponse(Team team, User user,
                                                        Integer teamMemberCnt, Integer activeMemberCnt,
                                                        List<String> teamMembers);
}
