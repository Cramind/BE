package com.groupstudy.domain.roomuser.service;

import com.groupstudy.domain.roomuser.dto.request.RoomUserInviteRequest;
import com.groupstudy.domain.roomuser.entity.RoomUser;
import com.groupstudy.domain.roomuser.repository.RoomUserRepository;
import com.groupstudy.domain.studyroom.entity.StudyRoom;
import com.groupstudy.domain.studyroom.repository.StudyRoomRepository;
import com.groupstudy.domain.user.entity.User;
import com.groupstudy.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomUserCommandService {
    private final StudyRoomRepository studyRoomRepository;
    private final RoomUserRepository roomUserRepository;
    private final UserRepository userRepository;

    public void insertRoomUser(Long roomId, RoomUserInviteRequest request){
        List<User> users = userRepository.findByEmails(request.getInvitedUsers());
        StudyRoom studyRoom = studyRoomRepository.findById(roomId).orElseThrow();

        List<RoomUser> roomUsers = users.stream()
                        .map(user -> createRoomUser(user, studyRoom))
                        .toList();

        roomUserRepository.saveAll(roomUsers);
    }

    private RoomUser createRoomUser(User user, StudyRoom studyRoom){
        return RoomUser.builder()
                .user(user)
                .studyRoom(studyRoom)
                .build();
    }
}
