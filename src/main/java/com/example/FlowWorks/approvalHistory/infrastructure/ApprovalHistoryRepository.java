package com.example.FlowWorks.approvalHistory.infrastructure;

import com.example.FlowWorks.approvalHistory.domain.ApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistory, Long> {

    List<ApprovalHistory> findByApprovalDocumentIdOrderByCreatedAtAsc(Long id);
}
