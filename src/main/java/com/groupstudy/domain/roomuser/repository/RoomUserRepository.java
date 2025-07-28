package com.groupstudy.domain.roomuser.repository;

import com.groupstudy.domain.roomuser.entity.RoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomUserRepository extends JpaRepository<RoomUser, Long>, CustomRoomUserRepository {
}
