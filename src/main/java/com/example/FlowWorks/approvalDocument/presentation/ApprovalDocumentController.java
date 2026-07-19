package com.example.FlowWorks.approvalDocument.presentation;

import com.example.FlowWorks.approvalDocument.application.ApprovalDocumentService;
import com.example.FlowWorks.approvalDocument.application.dto.ApprovalDocumentResponse;
import com.example.FlowWorks.approvalDocument.application.dto.CreateApprovalDocumentRequest;
import com.example.FlowWorks.approvalDocument.application.dto.RejectRequest;
import com.example.FlowWorks.approvalDocument.application.dto.UpdateApprovalDocumentRequest;
import com.example.FlowWorks.approvalDocument.domain.DocumentStatus;
import com.example.FlowWorks.approvalHistory.application.dto.ApprovalHistoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approval-documents")
@RequiredArgsConstructor
public class ApprovalDocumentController {

    private final ApprovalDocumentService approvalDocumentService;

    //결재문서 목록 조회 (내 기안함/결재함 등 필터)
    @GetMapping
    public ResponseEntity<List<ApprovalDocumentResponse>> getAllApprovalDocuments(@RequestParam(required = false) Long drafterId, @RequestParam(required = false) DocumentStatus status) {
        return ResponseEntity.ok(approvalDocumentService.findAll(drafterId, status));
    }

    //결재문서 상세 조회 (현재 라운드 결재선 포함)
    @GetMapping("/{id}")
    public ResponseEntity<ApprovalDocumentResponse> getApprovalDocument(@PathVariable Long id){
        return ResponseEntity.ok(approvalDocumentService.getDocument(id));
    }

    //기안서 작성 (DRAFT 임시저장)
    @PostMapping
    public ResponseEntity<ApprovalDocumentResponse> createApprovalDocument(@Valid @RequestBody CreateApprovalDocumentRequest request, Long drafterId){
        return ResponseEntity.status(HttpStatus.CREATED).body(approvalDocumentService.createApprovalDocument(request, drafterId));
    }

    //기안 내용 수정 (DRAFT 상태에서만)
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateApprovalDocument(@PathVariable Long id, @Valid @RequestBody UpdateApprovalDocumentRequest request, Long employeeId){

        approvalDocumentService.updateApprovalDocument(request, id, employeeId);

        return ResponseEntity.noContent().build();
    }

    //기안 상신
    @PostMapping("/{id}/submit")
    public ResponseEntity<Void> submitDocument(@PathVariable Long id, Long drafterId){
        approvalDocumentService.submit(id, drafterId);
        return ResponseEntity.noContent().build();
    }

    //기안 승인
    @PostMapping("/{id}/steps/{stepId}/approve")
    public ResponseEntity<Void> approveDocument(@PathVariable Long id, @PathVariable Long stepId, Long employeeId){
        approvalDocumentService.approve(id, stepId, employeeId);
        return ResponseEntity.noContent().build();
    }

    //기안 반려
    @PostMapping("/{id}/steps/{stepId}/reject")
    public ResponseEntity<Void> rejectDocument(@PathVariable Long id, @PathVariable Long stepId, Long employeeId, @Valid @RequestBody RejectRequest request){
        approvalDocumentService.reject(id, stepId, employeeId, request.comment());
        return ResponseEntity.noContent().build();
    }

    //반려 후 재상신
    @PostMapping("/{id}/resubmit")
    public ResponseEntity<Void> resubmitDocument(@PathVariable Long id, Long drafterId){
        approvalDocumentService.resubmit(id, drafterId);
        return ResponseEntity.noContent().build();
    }

    //이력 조회
    @GetMapping("/{id}/histories")
    public ResponseEntity<List<ApprovalHistoryResponse>> getApprovalHistory(@PathVariable Long id){
        return ResponseEntity.ok().body(approvalDocumentService.getApprovalHistory(id));
    }
}
