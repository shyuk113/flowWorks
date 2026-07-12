package com.example.FlowWorks.team.presentation;

import com.example.FlowWorks.team.application.TeamService;
import com.example.FlowWorks.team.application.dto.CreateTeamRequest;
import com.example.FlowWorks.team.application.dto.TeamResponse;
import com.example.FlowWorks.team.application.dto.UpdateTeamLeaderRequest;
import com.example.FlowWorks.team.application.dto.UpdateTeamNameRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    //TODO: employee 임시 설정 나중에 @AuthenticationPriciple 적용예정

    //팀 목록 조회
    @GetMapping
    public ResponseEntity<List<TeamResponse>> getTeams(){
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    //팀 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getTeam(@PathVariable Long id){
        return ResponseEntity.ok(teamService.getTeam(id));
    }

    //팀 생성
    @PostMapping
    public ResponseEntity<Void> createTeam(@Valid @RequestBody CreateTeamRequest request, Long employeeId){
        teamService.createTeam(request, employeeId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //팀명 수정
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateTeamName(@PathVariable Long id,@Valid @RequestBody UpdateTeamNameRequest request, Long employeeId){
        teamService.updateTeamName(request, id, employeeId);
        return ResponseEntity.noContent().build();
    }

    //팀장 지정 및 수정
    @PatchMapping("/{id}/leader")
    public ResponseEntity<Void> updateTeamLeader(@PathVariable Long id,@Valid @RequestBody UpdateTeamLeaderRequest request, Long employeeId){
        teamService.updateTeamLeader(request, id, employeeId);
        return ResponseEntity.noContent().build();
    }
}
