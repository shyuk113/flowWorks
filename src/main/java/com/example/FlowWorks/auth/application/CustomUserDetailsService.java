package com.example.FlowWorks.auth.application;

import com.example.FlowWorks.auth.domain.CustomUserDetails;
import com.example.FlowWorks.employee.domain.Employee;
import com.example.FlowWorks.employee.infrastructure.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email){
        Employee employee = employeeRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("존재하지 않는 계정입니다."));
        return new CustomUserDetails(employee);
    }
}
