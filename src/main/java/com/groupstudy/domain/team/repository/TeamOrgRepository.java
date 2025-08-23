package com.groupstudy.domain.team.repository;

import com.groupstudy.domain.team.entity.TeamOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamOrgRepository extends JpaRepository<TeamOrganization, Long> {
}
