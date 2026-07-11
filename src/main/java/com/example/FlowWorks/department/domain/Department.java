package com.example.FlowWorks.department.domain;

import com.example.FlowWorks.employee.domain.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_head_id", unique = true)
    private Employee departmentHead;

    @Builder
    private Department(String name, Employee departmentHead) {
        this.name = name;
        this.departmentHead = departmentHead;
    }

    public static Department createDepartment(String name, Employee departmentHead) {
        return Department.builder()
                .name(name)
                .departmentHead(departmentHead)
                .build();
    }

    public void updateDepartment(String name){
        this.name = name;
    }

    public void updateDepartmentHead(Employee departmentHead){
        this.departmentHead = departmentHead ;
    }
}
