package com.example.FlowWorks.approvalStep.application.dto;

import com.example.FlowWorks.approvalStep.domain.ApprovalStep;
import com.example.FlowWorks.approvalStep.domain.ApprovalStepStatus;
import com.example.FlowWorks.approvalStep.domain.StepType;

import java.time.LocalDateTime;

public record ApprovalStepSummary(Long stepId, int stepOrder, StepType stepType, Long approverId, String approverName, ApprovalStepStatus status, LocalDateTime approvedAt) {

    public static ApprovalStepSummary from(ApprovalStep step){
        return new ApprovalStepSummary(step.getId(), step.getStepOrder(), step.getStepType(),step.getApprover().getId(),step.getApprover().getName(), step.getStatus(), step.getApprovedAt());
    }
}
