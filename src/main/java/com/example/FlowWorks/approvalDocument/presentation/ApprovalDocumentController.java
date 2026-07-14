package com.example.FlowWorks.approvalDocument.presentation;

import com.example.FlowWorks.approvalDocument.application.ApprovalDocumentService;
import com.example.FlowWorks.approvalDocument.application.dto.ApprovalDocumentResponse;
import com.example.FlowWorks.approvalDocument.application.dto.CreateApprovalDocumentRequest;
import com.example.FlowWorks.approvalDocument.application.dto.UpdateApprovalDocumentRequest;
import com.example.FlowWorks.approvalDocument.application.dto.UpdateApprovalDocumentStatusRequest;
import com.example.FlowWorks.approvalDocument.domain.DocumentStatus;
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
    public ResponseEntity<Void> updateApprovalDocument(@PathVariable Long id, @RequestBody UpdateApprovalDocumentRequest request, Long employeeId){

        approvalDocumentService.updateApprovalDocument(request, id, employeeId);

        return ResponseEntity.noContent().build();
    }
}
