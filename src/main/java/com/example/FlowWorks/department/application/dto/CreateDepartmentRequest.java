package com.example.FlowWorks.department.application.dto;

import com.example.FlowWorks.employee.domain.Employee;

public record CreateDepartmentRequest(String name, Long departmentHeadId) {
}
