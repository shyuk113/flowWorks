package com.example.FlowWorks.approvalDocument.application.dto;

import com.example.FlowWorks.approvalDocument.domain.ApprovalDocument;
import com.example.FlowWorks.approvalDocument.domain.DocType;
import com.example.FlowWorks.approvalDocument.domain.DocumentStatus;
import com.example.FlowWorks.approvalStep.application.dto.ApprovalStepSummary;
import com.example.FlowWorks.approvalStep.domain.ApprovalStep;

import java.util.List;

public record ApprovalDocumentResponse(Long id, DocType docType, String title, String content, Long drafterId, int currentRound, DocumentStatus status, List<ApprovalStepSummary> steps) {

    public static ApprovalDocumentResponse from(ApprovalDocument approvalDocument) {
        return new ApprovalDocumentResponse(approvalDocument.getId(), approvalDocument.getDocType(), approvalDocument.getTitle(), approvalDocument.getContent(),
                approvalDocument.getDrafter().getId(), approvalDocument.getCurrentRound(), approvalDocument.getStatus(), List.of());
    }

    public static ApprovalDocumentResponse withSteps(ApprovalDocument approvalDocument, List<ApprovalStep> steps){
        return new ApprovalDocumentResponse(approvalDocument.getId(), approvalDocument.getDocType(), approvalDocument.getTitle(), approvalDocument.getContent(),
                approvalDocument.getDrafter().getId(), approvalDocument.getCurrentRound(), approvalDocument.getStatus(), steps.stream().map(ApprovalStepSummary::from).toList());
    }
}
