package com.example.FlowWorks.team.application.dto;

import jakarta.validation.constraints.*;

public record CreateTeamRequest(
        @NotBlank(message = "팀 이름은 한 글자 이상 열 글자 이하입니다.")
        @Size(min = 1, max = 10)
        String name,
        @NotNull(message = "부서 ID를 입력해주세요.")
        Long departmentId,
        
        Long teamLeaderId) {
}
