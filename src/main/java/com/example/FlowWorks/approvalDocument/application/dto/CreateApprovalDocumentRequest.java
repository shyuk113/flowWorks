package com.example.FlowWorks.approvalDocument.application.dto;

import com.example.FlowWorks.approvalDocument.domain.DocType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateApprovalDocumentRequest(
        @NotNull
        DocType docType,
        @NotBlank
        String title,
        @NotBlank
        String content) {
}
