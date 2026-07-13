package com.example.FlowWorks.employee.application.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateEmployeeNameRequest(
        @NotBlank
        String name) {
}
