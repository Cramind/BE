package com.groupstudy.domain.roomuser.service;

import com.groupstudy.domain.roomuser.dto.response.StudyRoomListResponse;
import com.groupstudy.domain.roomuser.repository.RoomUserRepository;
import com.groupstudy.domain.studyroom.entity.StudyRoom;
import com.groupstudy.domain.studyroom.repository.StudyRoomRepository;
import com.groupstudy.global.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomUserQueryService {
    private final RoomUserRepository roomUserRepository;
    private final StudyRoomRepository studyRoomRepository;
//
//    public StudyRoomListResponse selectParticipatedStudyRooms(CustomUserDetails customUserDetails){
//        List<Long> studyRoomIds = roomUserRepository.findStudyRoomByUserId(customUserDetails.getUserId());
//
//    }
}
