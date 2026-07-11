package com.example.FlowWorks.department.infrastructure;

import com.example.FlowWorks.department.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
