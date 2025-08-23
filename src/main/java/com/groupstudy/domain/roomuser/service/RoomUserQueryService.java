package com.groupstudy.domain.roomuser.service;

import com.groupstudy.domain.roomuser.repository.RoomUserRepository;
import com.groupstudy.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomUserQueryService {
    private final RoomUserRepository roomUserRepository;
    private final TeamRepository teamRepository;
//
//    public StudyRoomListResponse selectParticipatedStudyRooms(CustomUserDetails customUserDetails){
//        List<Long> studyRoomIds = roomUserRepository.findStudyRoomByUserId(customUserDetails.getUserId());
//
//    }
}
