package com.example.FlowWorks.rank.presentation;

import com.example.FlowWorks.rank.application.RankService;
import com.example.FlowWorks.rank.application.dto.CreateRankRequest;
import com.example.FlowWorks.rank.application.dto.RankResponse;
import com.example.FlowWorks.rank.application.dto.UpdateRankRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ranks")
@RequiredArgsConstructor
public class RankController {

    private final RankService rankService;

    //직급 목록 조회
    @GetMapping
    public ResponseEntity<List<RankResponse>> getRanks(){
        return ResponseEntity.ok(rankService.getRanks());
    }

    //직급 생성
    @PostMapping
    public ResponseEntity<RankResponse> createRank(@Valid @RequestBody CreateRankRequest request, Long employeeId){
        return ResponseEntity.status(HttpStatus.CREATED).body(rankService.createRank(request, employeeId));
    }

    //직급명/레벨 수정
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateRank(@PathVariable Long id, @Valid @RequestBody UpdateRankRequest request, Long employeeId){
        rankService.updateRank(request, id, employeeId);
        return ResponseEntity.noContent().build();
    }
}
