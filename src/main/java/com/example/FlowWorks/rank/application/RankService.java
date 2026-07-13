package com.example.FlowWorks.rank.application;

import com.example.FlowWorks.employee.domain.Employee;
import com.example.FlowWorks.employee.infrastructure.EmployeeRepository;
import com.example.FlowWorks.rank.application.dto.CreateRankRequest;
import com.example.FlowWorks.rank.application.dto.RankResponse;
import com.example.FlowWorks.rank.application.dto.UpdateRankRequest;
import com.example.FlowWorks.rank.domain.Rank;
import com.example.FlowWorks.rank.infrastructure.RankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankService {

    private final RankRepository rankRepository;

    private static final int MANAGER_MIN_LEVEL = 5; //임시 임계값
    private final EmployeeRepository employeeRepository;

    //직급 목록 조회
    @Transactional(readOnly = true)
    public List<RankResponse> getRanks(){
        return rankRepository.findAll().stream().map(RankResponse::from).toList();
    }

    //직급 생성
    @Transactional
    public RankResponse createRank(CreateRankRequest request, Long employeeId){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 직원입니다."));

        if(employee.getRank().getLevel() < MANAGER_MIN_LEVEL){
            throw new AccessDeniedException("직급 생성 권한이 없습니다.");
        }

        Rank rank = Rank.createRank(request.name(), request.level());

        rankRepository.save(rank);

        return RankResponse.from(rank);
    }

    //직급명/레벨 수정
    @Transactional
    public void updateRank(UpdateRankRequest request, Long targetId, Long employeeId){

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 직원입니다."));

        if(employee.getRank().getLevel() < MANAGER_MIN_LEVEL){
            throw new AccessDeniedException("직급 수정 권한이 없습니다.");
        }

        Rank rank = rankRepository.findById(targetId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 직급입니다."));

        rank.updateRank(request.name(), request.level());
    }
}
