package com.example.FlowWorks.approvalStep.infrastructure;

import com.example.FlowWorks.approvalStep.domain.ApprovalStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalStepRepository extends JpaRepository<ApprovalStep, Long> {

    List<ApprovalStep> findByApprovalDocumentIdAndRoundNumber(Long approvalDocumentId, int roundNumber);
}
