package com.example.FlowWorks.approvalDocument.application.dto;

import com.example.FlowWorks.approvalDocument.domain.ApprovalDocument;
import com.example.FlowWorks.approvalDocument.domain.DocType;
import com.example.FlowWorks.approvalDocument.domain.DocumentStatus;

public record ApprovalDocumentResponse(Long id, DocType docType, String title, String content, Long drafterId, int currentRound, DocumentStatus status) {

    public static ApprovalDocumentResponse from(ApprovalDocument approvalDocument) {
        return new ApprovalDocumentResponse(approvalDocument.getId(), approvalDocument.getDocType(), approvalDocument.getTitle(), approvalDocument.getContent(),
                approvalDocument.getDrafter().getId(), approvalDocument.getCurrentRound(), approvalDocument.getStatus());
    }
}
