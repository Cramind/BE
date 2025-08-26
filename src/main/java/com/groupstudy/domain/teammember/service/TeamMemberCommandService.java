package com.groupstudy.domain.teammember.service;

import com.groupstudy.domain.teammember.entity.TeamMember;
import com.groupstudy.domain.teammember.repository.TeamMemberRepository;
import com.groupstudy.domain.team.entity.InviteCode;
import com.groupstudy.domain.team.entity.Team;
import com.groupstudy.domain.team.repository.InviteCodeRepository;
import com.groupstudy.domain.team.repository.TeamRepository;
import com.groupstudy.domain.user.entity.User;
import com.groupstudy.domain.user.repository.UserRepository;
import com.groupstudy.global.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamMemberCommandService {
    private final InviteCodeRepository inviteCodeRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;

    public String getOrCreateInviteCode(Long teamId){
        Team team = teamRepository.findById(teamId).orElseThrow();
        String optionalInviteCode = inviteCodeRepository.findByStudyRoomId(team.getId());
        String BASE_URL = "http://localhost:5173/invite/";
        if (optionalInviteCode == null){
            InviteCode inviteCode = InviteCode.builder()
                    .code(UUID.randomUUID().toString())
                    .team(team)
                    .expiresAt(LocalDateTime.now().plusDays(7))
                    .build();
            inviteCodeRepository.save(inviteCode);
            return BASE_URL + inviteCode.getCode();
        }

        return BASE_URL + optionalInviteCode;
    }

    public void insertRoomUser(String inviteCode, CustomUserDetails customUserDetails){
        User user = userRepository.findByEmail(customUserDetails.getEmail());
        Long studyRoomId = inviteCodeRepository.findStudyRoomByCode(inviteCode);

        Team team = teamRepository.findById(studyRoomId).orElseThrow();

        TeamMember roomUsers = createRoomUser(user, team);

        teamMemberRepository.save(roomUsers);
    }

    private TeamMember createRoomUser(User user, Team team){
        return new TeamMember(team, user);
    }
}
