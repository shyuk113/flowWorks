package com.example.FlowWorks.department.presentation;

import com.example.FlowWorks.department.application.DepartmentService;
import com.example.FlowWorks.department.application.dto.CreateDepartmentRequest;
import com.example.FlowWorks.department.application.dto.DepartmentResponse;
import com.example.FlowWorks.department.application.dto.UpdateDepartmentHeadRequest;
import com.example.FlowWorks.department.application.dto.UpdateDepartmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    //부서 목록 조회
    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments(){
        return ResponseEntity.ok(departmentService.getDepartments());
    }

    //부서 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable Long id){
        return ResponseEntity.ok(departmentService.getDepartment(id));
    }

    //부서 생성
    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(@RequestBody CreateDepartmentRequest request, Long employeeId){
        return ResponseEntity.ok(departmentService.addDepartment(request, employeeId));
    }

    //부서명 등 기본 정보 수정
    @PatchMapping("/{id}")
    public ResponseEntity<Void>  updateDepartment(@PathVariable Long id, @RequestBody UpdateDepartmentRequest request, Long employeeId){
        departmentService.updateDepartment(request, id, employeeId);
        return ResponseEntity.ok().build();
    }

    //부서장 지정/변경
    @PatchMapping("/{id}/head")
    public ResponseEntity<Void> updateDepartmentHead(@PathVariable Long id, @RequestBody UpdateDepartmentHeadRequest request, Long employeeId){
        departmentService.updateDepartmentHead(request, id, employeeId);
        return ResponseEntity.ok().build();
    }
}
