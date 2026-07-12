package com.example.FlowWorks.team.application.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateTeamLeaderRequest(
        @NotNull(message = "팀리더를 지정해주세요.")
        Long teamLeaderId) {
}
