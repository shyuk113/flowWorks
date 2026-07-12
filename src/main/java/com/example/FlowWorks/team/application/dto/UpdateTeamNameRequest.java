package com.example.FlowWorks.team.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTeamNameRequest(
        @NotBlank(message = "팀 이름은 한 글자 이상 열 글자 이하 입니다.")
        @Size(min = 1, max = 10)
        String name) {
}
