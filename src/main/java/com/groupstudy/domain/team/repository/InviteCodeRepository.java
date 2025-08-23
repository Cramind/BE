package com.groupstudy.domain.team.repository;

import com.groupstudy.domain.team.entity.InviteCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteCodeRepository extends JpaRepository<InviteCode, String>, CustomInviteCodeRepository {
}
