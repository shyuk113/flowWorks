package com.example.FlowWorks.approvalDocument.application.dto;

import com.example.FlowWorks.approvalDocument.domain.DocType;

public record CreateApprovalDocumentRequest(DocType docType, String title, String content) {
}
