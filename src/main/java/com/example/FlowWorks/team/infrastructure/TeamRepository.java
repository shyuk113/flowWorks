package com.example.FlowWorks.team.infrastructure;

import com.example.FlowWorks.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team,Long> {
}
