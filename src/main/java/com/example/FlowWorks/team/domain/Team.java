package com.example.FlowWorks.team.domain;

import com.example.FlowWorks.department.domain.Department;
import com.example.FlowWorks.employee.domain.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_leader_id", unique = true)
    private Employee teamLeader;

    @Builder
    private Team(String name, Department department, Employee teamLeader) {
        this.name = name;
        this.department = department;
        this.teamLeader = teamLeader;
    }

    public static Team createTeam(String name, Department department, Employee teamLeader) {
        return Team.builder()
                .name(name)
                .department(department)
                .teamLeader(teamLeader)
                .build();
    }

    public void updateTeamName(String name){
        this.name = name;
    }

    public void updateTeamLeader(Employee teamLeader){
        this.teamLeader = teamLeader;
    }
}
