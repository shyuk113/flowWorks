package com.example.FlowWorks.employee.presentation;

import com.example.FlowWorks.employee.application.EmployeeService;
import com.example.FlowWorks.employee.application.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    //TODO: @AuthenticalPrinciple 추가 예정

    //직원 검색/목록 조회
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees(){
        return ResponseEntity.ok(employeeService.getEmployees());
    }

    //직원 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployee(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.getEmployee(id));
    }

    //직원 등록 (teamId, rankId 필수)
    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody CreateEmployeeRequest request, Long employeeId){
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(request, employeeId));
    }

    //기본 정보 수정
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateEmployeeName(@PathVariable Long id, @Valid @RequestBody UpdateEmployeeNameRequest request, Long employeeId){
        employeeService.updateEmployeeName(request, id, employeeId);
        return ResponseEntity.noContent().build();
    }

    //소속팀 변경 (전배)
    @PatchMapping("/{id}/team")
    public ResponseEntity<Void> updateEmployeeTeam(@PathVariable Long id, @Valid @RequestBody UpdateEmployeeTeamRequest request, Long employeeId){
        employeeService.updateEmployeeTeam(request, id, employeeId);
        return ResponseEntity.noContent().build();
    }

    //직급 변경
    @PatchMapping("/{id}/rank")
    public ResponseEntity<Void> updateEmployeeRank(@PathVariable Long id, @Valid @RequestBody UpdateEmployeeRankRequest request, Long employeeId){
        employeeService.updateEmployeeRank(request, id, employeeId);
        return ResponseEntity.noContent().build();
    }

    //재직상태 변경 (휴직/복직/퇴사)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateEmployeeStatus(@PathVariable Long id, @Valid @RequestBody UpdateEmployeeStatusRequest request, Long employeeId){
        employeeService.updateEmployeeStatus(request, id, employeeId);
        return ResponseEntity.noContent().build();
    }
}
