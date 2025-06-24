package com.groupstudy.domain.roomuser.service;

import com.groupstudy.domain.roomuser.dto.request.RoomUserInviteRequest;
import com.groupstudy.domain.roomuser.entity.RoomUser;
import com.groupstudy.domain.roomuser.repository.RoomUserRepository;
import com.groupstudy.domain.studyroom.entity.InviteCode;
import com.groupstudy.domain.studyroom.entity.StudyRoom;
import com.groupstudy.domain.studyroom.repository.InviteCodeRepository;
import com.groupstudy.domain.studyroom.repository.StudyRoomRepository;
import com.groupstudy.domain.user.entity.User;
import com.groupstudy.domain.user.repository.UserRepository;
import com.groupstudy.global.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomUserCommandService {
    private final InviteCodeRepository inviteCodeRepository;
    private final StudyRoomRepository studyRoomRepository;
    private final RoomUserRepository roomUserRepository;
    private final UserRepository userRepository;

    public String getOrCreateInviteCode(Long roomId){
        StudyRoom studyRoom = studyRoomRepository.findById(roomId).orElseThrow();
        String optionalInviteCode = inviteCodeRepository.findByStudyRoomId(studyRoom.getId());
        String BASE_URL = "http://localhost:5173/invite/";
        if (optionalInviteCode == null){
            InviteCode inviteCode = InviteCode.builder()
                    .code(UUID.randomUUID().toString())
                    .studyRoom(studyRoom)
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

        StudyRoom studyRoom = studyRoomRepository.findById(studyRoomId).orElseThrow();

        RoomUser roomUsers = createRoomUser(user, studyRoom);

        roomUserRepository.save(roomUsers);
    }

    private RoomUser createRoomUser(User user, StudyRoom studyRoom){
        return RoomUser.builder()
                .user(user)
                .studyRoom(studyRoom)
                .build();
    }
}
