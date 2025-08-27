package com.groupstudy.domain.team.repository;

import com.groupstudy.domain.team.entity.TeamRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepoRepository extends JpaRepository<TeamRepo, Long> {
}
