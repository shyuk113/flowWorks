package com.example.FlowWorks.team.application.dto;

import com.example.FlowWorks.department.domain.Department;
import com.example.FlowWorks.employee.domain.Employee;
import com.example.FlowWorks.team.domain.Team;

public record TeamResponse(Long id, String name, Long departmentId, Long employeeId) {

    public static TeamResponse from(Team team) {
        return new TeamResponse(team.getId(), team.getName(), team.getDepartment().getId(), team.getTeamLeader().getId());
    }
}
