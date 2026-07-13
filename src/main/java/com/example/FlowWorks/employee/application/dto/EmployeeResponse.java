package com.example.FlowWorks.employee.application.dto;

import com.example.FlowWorks.employee.domain.Employee;
import com.example.FlowWorks.employee.domain.EmployeeStatus;

public record EmployeeResponse(Long id, String name, EmployeeStatus status, Long teamId, Long rankId) {

    public static EmployeeResponse from(Employee employee){
        return new EmployeeResponse(employee.getId(), employee.getName(), employee.getStatus(),employee.getTeam().getId(), employee.getRank().getId());
    }
}
