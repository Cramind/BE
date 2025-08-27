package com.groupstudy.domain.teammember.service;

import com.groupstudy.domain.teammember.dto.response.TeamMemberInfoResponse;
import com.groupstudy.domain.teammember.repository.TeamMemberRepository;
import com.groupstudy.domain.team.repository.TeamRepository;
import com.groupstudy.global.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamMemberQueryService {
    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;

}
