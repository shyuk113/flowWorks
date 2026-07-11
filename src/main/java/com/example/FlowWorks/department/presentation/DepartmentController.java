package com.example.FlowWorks.department.presentation;

import com.example.FlowWorks.department.application.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    //부서 목록 조회



    //부서 상세 조회



    //부서 생성



    //부서명 등 기본 정보 수정



    //부서장 지정/변경
}
