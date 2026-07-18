package com.example.FlowWorks.approvalHistory.infrastructure;

import com.example.FlowWorks.approvalHistory.domain.ApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistory, Long> {
}
