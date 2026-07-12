package com.example.FlowWorks.department.application.dto;

import com.example.FlowWorks.department.domain.Department;

public record DepartmentResponse(Long departmentId, String name, Long departmentHeadId) {

    public static DepartmentResponse from(Department department) {
        return new DepartmentResponse(department.getId(),department.getName(), department.getDepartmentHead().getId());
    }
}
