package com.example.FlowWorks.approvalDocument.application.dto;

import jakarta.validation.constraints.NotBlank;

public record RejectRequest(
        @NotBlank
        String comment) {
}
