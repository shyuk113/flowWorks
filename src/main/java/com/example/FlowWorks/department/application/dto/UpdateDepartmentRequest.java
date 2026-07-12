package com.example.FlowWorks.department.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateDepartmentRequest(
        @NotBlank(message = "부서명을 한글자 이상 열 글자 이하로 지정해주세요.")
        @Size(min = 1, max = 10)
        String name) {
}
