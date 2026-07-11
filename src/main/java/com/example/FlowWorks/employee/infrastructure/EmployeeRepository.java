package com.example.FlowWorks.employee.infrastructure;

import com.example.FlowWorks.employee.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
