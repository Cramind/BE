package com.groupstudy.domain.studyroom.repository;

import com.groupstudy.domain.studyroom.entity.InviteCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteCodeRepository extends JpaRepository<InviteCode, String>, CustomInviteCodeRepository {
}
