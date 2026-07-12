package com.example.FlowWorks.department.application.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateDepartmentHeadRequest(
        @NotNull(message = "부서장을 지정해 주세요.")
        Long departmentHeadId) {
}
