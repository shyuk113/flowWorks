package com.example.FlowWorks.rank.infrastructure;

import com.example.FlowWorks.rank.domain.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<Rank, Long> {
}
