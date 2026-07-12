package com.example.FlowWorks.department.application;

import com.example.FlowWorks.department.application.dto.CreateDepartmentRequest;
import com.example.FlowWorks.department.application.dto.DepartmentResponse;
import com.example.FlowWorks.department.application.dto.UpdateDepartmentHeadRequest;
import com.example.FlowWorks.department.application.dto.UpdateDepartmentRequest;
import com.example.FlowWorks.department.domain.Department;
import com.example.FlowWorks.department.infrastructure.DepartmentRepository;
import com.example.FlowWorks.employee.domain.Employee;
import com.example.FlowWorks.employee.infrastructure.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    private static final int DEPARTMENT_MANAGE_MIN_RANK = 5; //직급 레벨 임계값 임시 설정

    //부서 목록 조회
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getDepartments(){
        return departmentRepository.findAll().stream().map(DepartmentResponse::from).toList();
    }

    //부서 상세 조회
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartment(Long departmentId){
        Department department = departmentRepository.findById(departmentId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 부서입니다."));
        return DepartmentResponse.from(department);
    }

    //부서 생성
    @Transactional
    public DepartmentResponse addDepartment(CreateDepartmentRequest request, Long employeeId){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 직원입니다."));
        if(employee.getRank().getLevel() < DEPARTMENT_MANAGE_MIN_RANK){
            throw new AccessDeniedException("부서 생성 권한이 없습니다.");
        }

        Employee departmentHead = employeeRepository.findById(request.departmentHeadId()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 직원입니다."));

        Department department = Department.createDepartment(request.name(), departmentHead);
        departmentRepository.save(department);
        return DepartmentResponse.from(department);
    }

    //부서명 등 기본 정보 수정
    @Transactional
    public void updateDepartment(UpdateDepartmentRequest request, Long departmentId, Long employeeId){
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 직원입니다."));
        if(employee.getRank().getLevel() < DEPARTMENT_MANAGE_MIN_RANK){
            throw new AccessDeniedException("부서 생성 권한이 없습니다.");
        }

        Department department = departmentRepository.findById(departmentId).orElseThrow(()->new IllegalArgumentException("존재 하지 않는 부서입니다."));

        department.updateDepartment(request.name());
    }

    //부서장 지정/변경
    @Transactional
    public void updateDepartmentHead(UpdateDepartmentHeadRequest request, Long departmentId, Long employeeId){
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 직원입니다."));
        if(employee.getRank().getLevel() < DEPARTMENT_MANAGE_MIN_RANK){
            throw new AccessDeniedException("부서 생성 권한이 없습니다.");
        }

        Department department = departmentRepository.findById(departmentId).orElseThrow(()->new IllegalArgumentException("존재 하지 않는 부서입니다."));

        Employee newDepartmentHead = employeeRepository.findById(request.departmentHeadId())
                        .orElseThrow(()->new IllegalArgumentException("존재 하지 않는 직원입니다."));

        department.updateDepartmentHead(newDepartmentHead);
    }
}
