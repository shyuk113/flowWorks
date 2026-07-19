package com.example.FlowWorks.approvalHistory.application.dto;

import com.example.FlowWorks.approvalHistory.domain.Action;
import com.example.FlowWorks.approvalHistory.domain.ApprovalHistory;

import java.time.LocalDateTime;

public record ApprovalHistoryResponse(Long actorId, String actorName, Action action, String comment, LocalDateTime createdAt) {

    public static ApprovalHistoryResponse from(ApprovalHistory approvalHistory) {
        return new ApprovalHistoryResponse(approvalHistory.getActor().getId(), approvalHistory.getActor().getName(), approvalHistory.getAction(), approvalHistory.getComment(),
                approvalHistory.getCreatedAt());
    }
}
