package com.example.FlowWorks.employee.domain;

import com.example.FlowWorks.rank.domain.Rank;
import com.example.FlowWorks.team.domain.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rank_id", nullable = false)
    private Rank rank;

    @Builder
    private Employee(String email, String password, String name, EmployeeStatus status, Team team, Rank rank) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.status = status;
        this.team = team;
        this.rank = rank;
    }

    public static Employee createEmployee(String email, String password, String name, Team team, Rank rank) {
        return Employee.builder()
                .email(email)
                .password(password)
                .name(name)
                .status(EmployeeStatus.ACTIVE)
                .team(team)
                .rank(rank)
                .build();
    }

    public void updateEmployeeName(String name) {
        this.name = name;
    }

    public void updateEmployeeStatus(EmployeeStatus status) {
        this.status = status;
    }

    public void updateEmployeeTeam(Team team) {
        this.team = team;
    }

    public void updateEmployeeRank(Rank rank) {
        this.rank = rank;
    }

}
