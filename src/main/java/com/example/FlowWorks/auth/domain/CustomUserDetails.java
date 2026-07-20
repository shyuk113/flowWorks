package com.example.FlowWorks.auth.domain;

import com.example.FlowWorks.department.domain.Department;
import com.example.FlowWorks.employee.domain.Employee;
import com.example.FlowWorks.employee.domain.EmployeeStatus;
import com.example.FlowWorks.team.domain.Team;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Employee employee;

    public CustomUserDetails(Employee employee) {
        this.employee = employee;
    }

    public Long getEmployeeId(){
        return employee.getId();
    }

    @Override
    public String getUsername() {
        return employee.getEmail();
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_LEVEL_"+ employee.getRank().getLevel()));

        Team team =  employee.getTeam();
        if(team.getTeamLeader()!= null &&team.getTeamLeader().getId().equals(employee.getId())){
            authorities.add(new SimpleGrantedAuthority("ROLE_TEAM_LEADER"));
        }

        Department department = team.getDepartment();
        if(department.getDepartmentHead()!=null && department.getDepartmentHead().getId().equals(employee.getId())){
            authorities.add(new SimpleGrantedAuthority("ROLE_DEPARTMENT_HEAD"));
        }

        return authorities;
    }

    @Override
    public boolean isEnabled(){
        return employee.getStatus() == EmployeeStatus.ACTIVE;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
