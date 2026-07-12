package com.example.FlowWorks.department.application.dto;

import jakarta.validation.constraints.*;

public record CreateDepartmentRequest(
        @NotBlank(message = "부서명을 한글자 이상 열 글자 이하로 지정해주세요.")
        @Size(min = 1, max = 10)
        String name,
        @NotNull(message = "부서장을 지정해 주세요.")
        Long departmentHeadId) {
}
