package com.groupstudy.domain.teammember.service;

import com.groupstudy.domain.teammember.repository.TeamMemberRepository;
import com.groupstudy.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamMemberQueryService {
    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
//
//    public StudyRoomListResponse selectParticipatedStudyRooms(CustomUserDetails customUserDetails){
//        List<Long> studyRoomIds = roomUserRepository.findStudyRoomByUserId(customUserDetails.getUserId());
//
//    }
}
