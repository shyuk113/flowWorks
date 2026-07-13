package com.example.FlowWorks.employee.application.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateEmployeeTeamRequest(
        @NotNull
        Long teamId) {
}
