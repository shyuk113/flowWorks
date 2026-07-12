package com.example.FlowWorks.team.application;

import com.example.FlowWorks.department.domain.Department;
import com.example.FlowWorks.department.infrastructure.DepartmentRepository;
import com.example.FlowWorks.employee.domain.Employee;
import com.example.FlowWorks.employee.infrastructure.EmployeeRepository;
import com.example.FlowWorks.team.application.dto.CreateTeamRequest;
import com.example.FlowWorks.team.application.dto.TeamResponse;
import com.example.FlowWorks.team.application.dto.UpdateTeamLeaderRequest;
import com.example.FlowWorks.team.application.dto.UpdateTeamNameRequest;
import com.example.FlowWorks.team.domain.Team;
import com.example.FlowWorks.team.infrastructure.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;

    private static final int MANAGE_MIN_RANK = 5; //권한 랭크 임계값 임시 생성
    private final DepartmentRepository departmentRepository;

    //팀 목록 조회(부서별 필터)
    @Transactional(readOnly = true)
    public List<TeamResponse> getAllTeams(){
        return teamRepository.findAll().stream().map(TeamResponse::from).toList();
    }

    //팀 상세 조회
    @Transactional(readOnly = true)
    public TeamResponse getTeam(Long teamId){
        Team team = teamRepository.findById(teamId).orElseThrow(()-> new IllegalArgumentException("존재 하지 않는 팀입니다."));
        return TeamResponse.from(team);
    }

    //팀 생성 (departmentId 필수, leader는 미지정 가능)
    @Transactional
    public TeamResponse createTeam(CreateTeamRequest request, Long employeeId){
        Employee admin = employeeRepository.findById(employeeId).orElseThrow(()-> new IllegalArgumentException("존재 하지 않는 직원입니다."));
        if(admin.getRank().getLevel()<MANAGE_MIN_RANK){
            throw new AccessDeniedException("팀 생성 권한이 없습니다.");
        }
        Employee teamLeader = employeeRepository.findById(request.teamLeaderId()).orElseThrow(()-> new IllegalArgumentException("존재 하지않는 직원입니다."));

        Department department = departmentRepository.findById(request.departmentId()).orElseThrow(()-> new IllegalArgumentException("존재 하지 않는 부서입니다."));
        Team team = Team.createTeam(request.name(), department, teamLeader);
        teamRepository.save(team);
        return TeamResponse.from(team);
    }

    //팀명 수정
    @Transactional
    public void updateTeamName(UpdateTeamNameRequest request, Long teamId, Long employeeId){

        Employee admin = employeeRepository.findById(employeeId).orElseThrow(()-> new IllegalArgumentException("존재 하지 않는 직원입니다."));
        if(admin.getRank().getLevel()<MANAGE_MIN_RANK){
            throw new AccessDeniedException("팀명 수정 권한이 없습니다.");
        }
        Team team = teamRepository.findById(teamId).orElseThrow(()->new IllegalArgumentException("존재 하지 않는 팀입니다."));
        team.updateTeamName(request.name());
    }

    //팀장 지정/변경
    @Transactional
    public void updateTeamLeader(UpdateTeamLeaderRequest request, Long teamId, Long employeeId){
        Employee admin = employeeRepository.findById(employeeId).orElseThrow(()-> new IllegalArgumentException("존재 하지 않는 직원입니다."));
        if(admin.getRank().getLevel()<MANAGE_MIN_RANK){
            throw new AccessDeniedException("팀장 지정 권한이 없습니다.");
        }

        Team team = teamRepository.findById(teamId).orElseThrow(()->new IllegalArgumentException("존재 하지 않는 팀입니다."));

        Employee teamLeader = employeeRepository.findById(request.teamLeaderId()).orElseThrow(()-> new IllegalArgumentException("존재 하지 않는 직원 입니다."));

        team.updateTeamLeader(teamLeader);
    }
}
