package com.example.FlowWorks.employee.application.dto;

import com.example.FlowWorks.employee.domain.EmployeeStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateEmployeeStatusRequest(
        @NotNull
        EmployeeStatus status) {
}
