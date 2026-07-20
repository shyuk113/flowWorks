package com.example.FlowWorks.employee.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateEmployeeRequest(
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String name,
        @NotNull
        Long teamId,
        @NotNull
        Long rankId) {
}
