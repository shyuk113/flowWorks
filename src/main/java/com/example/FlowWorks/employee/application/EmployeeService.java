package com.example.FlowWorks.employee.application;

import com.example.FlowWorks.employee.application.dto.*;
import com.example.FlowWorks.employee.domain.Employee;
import com.example.FlowWorks.employee.infrastructure.EmployeeRepository;
import com.example.FlowWorks.rank.domain.Rank;
import com.example.FlowWorks.rank.infrastructure.RankRepository;
import com.example.FlowWorks.team.domain.Team;
import com.example.FlowWorks.team.infrastructure.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TeamRepository teamRepository;
    private final RankRepository rankRepository;

    private static final int MANAGER_MIN_RANK = 5;
    private final PasswordEncoder passwordEncoder;

    //직원 목록 조회
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getEmployees(){
        return employeeRepository.findAll().stream().map(EmployeeResponse::from).toList();
    }

    //직원 상세 조회
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployee(Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 직원입니다."));
        return EmployeeResponse.from(employee);
    }

    //직원 등록 (teamId, rankId 필수)
    @Transactional
    public EmployeeResponse createEmployee(CreateEmployeeRequest request, Long employeeId){

        Employee admin = employeeRepository.findById(employeeId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 직원입니다."));

        if(admin.getRank().getLevel() < MANAGER_MIN_RANK){
            throw new AccessDeniedException("직원 생성 권한이 없습니다.");
        }

        if(employeeRepository.existsByEmail(request.email())){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Team team = teamRepository.findById(request.teamId()).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 팀입니다."));

        Rank rank = rankRepository.findById(request.rankId()).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 직급입니다."));

        Employee employee = Employee.createEmployee(request.email(), passwordEncoder.encode(request.password()), request.name(), team, rank);

        employeeRepository.save(employee);

        return  EmployeeResponse.from(employee);
    }

    //직원 이름 수정
    @Transactional
    public void updateEmployeeName(UpdateEmployeeNameRequest request,Long targetId, Long employeeId){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 직원입니다."));

        if(employee.getRank().getLevel() < MANAGER_MIN_RANK){
            throw new AccessDeniedException("직원 이름 수정 권한이 없습니다.");
        }

        Employee target = employeeRepository.findById(targetId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 직원입니다."));

        target.updateEmployeeName(request.name());
    }

    //직원 팀 수정
    @Transactional
    public void updateEmployeeTeam(UpdateEmployeeTeamRequest request,Long targetId, Long employeeId){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 직원입니다."));

        if(employee.getRank().getLevel() < MANAGER_MIN_RANK){
            throw new AccessDeniedException("직원의 팀 수정 권한이 없습니다.");
        }

        Employee target = employeeRepository.findById(targetId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 직원입니다."));

        Team team = teamRepository.findById(request.teamId()).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 팀입니다."));

        target.updateEmployeeTeam(team);
    }

    //직원 직급 수정
    @Transactional
    public void updateEmployeeRank(UpdateEmployeeRankRequest request, Long targetId, Long employeeId){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 직원입니다."));

        if(employee.getRank().getLevel() < MANAGER_MIN_RANK){
            throw new AccessDeniedException("직원의 직급 수정 권한이 없습니다.");
        }

        Employee target = employeeRepository.findById(targetId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 직원입니다."));

        Rank rank = rankRepository.findById(request.rankId()).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 직급입니다."));
        target.updateEmployeeRank(rank);
    }

    //재직 상태 변경
    @Transactional
    public void updateEmployeeStatus(UpdateEmployeeStatusRequest request, Long targetId, Long employeeId){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 직원입니다."));

        if(employee.getRank().getLevel() < MANAGER_MIN_RANK){
            throw new AccessDeniedException("직원의 재직 상태 수정 권한이 없습니다.");
        }

        Employee target = employeeRepository.findById(targetId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 직원입니다."));

        target.updateEmployeeStatus(request.status());
    }
}
