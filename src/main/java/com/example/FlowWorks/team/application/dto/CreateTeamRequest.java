package com.example.FlowWorks.team.application.dto;

public record CreateTeamRequest(String name, Long departmentId, Long teamLeaderId) {
}
