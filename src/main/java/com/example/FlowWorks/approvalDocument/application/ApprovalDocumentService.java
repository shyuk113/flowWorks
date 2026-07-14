package com.example.FlowWorks.approvalDocument.application;

import com.example.FlowWorks.approvalDocument.application.dto.ApprovalDocumentResponse;
import com.example.FlowWorks.approvalDocument.application.dto.CreateApprovalDocumentRequest;
import com.example.FlowWorks.approvalDocument.application.dto.UpdateApprovalDocumentRequest;
import com.example.FlowWorks.approvalDocument.domain.ApprovalDocument;
import com.example.FlowWorks.approvalDocument.domain.DocumentStatus;
import com.example.FlowWorks.approvalDocument.infrastructure.ApprovalDocumentRepository;
import com.example.FlowWorks.employee.domain.Employee;
import com.example.FlowWorks.employee.infrastructure.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalDocumentService {

    private final ApprovalDocumentRepository approvalDocumentRepository;
    private final EmployeeRepository employeeRepository;

    private static final int MANAGER_MIN_RANK = 5; //임시 설정

    //결재문서 목록 조회 (내 기안함/결재함 등 필터)
    @Transactional(readOnly = true)
    public List<ApprovalDocumentResponse> findAll(Long employeeId, DocumentStatus status) {
        return approvalDocumentRepository.findAll().stream()
                .filter(doc -> employeeId ==null || doc.getDrafter().getId().equals(employeeId))
                .filter(doc-> status == null || doc.getStatus().equals(status))
                .map(ApprovalDocumentResponse::from)
                .toList();
    }

    //결재문서 상세 조회 (현재 라운드 결재선 포함)
    @Transactional(readOnly = true)
    public ApprovalDocumentResponse getDocument(Long id){
        ApprovalDocument approvalDocument = approvalDocumentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 결재입니다."));
        return ApprovalDocumentResponse.from(approvalDocument);
    }

    //기안서 작성 (DRAFT 임시저장)
    @Transactional
    public ApprovalDocumentResponse createApprovalDocument(CreateApprovalDocumentRequest request, Long drafterId){

        Employee drafter = employeeRepository.findById(drafterId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 직원입니다."));

        ApprovalDocument approvalDocument = ApprovalDocument.createApprovalDocument(request.docType(), request.title(), request.content(), drafter);

        approvalDocumentRepository.save(approvalDocument);

        return ApprovalDocumentResponse.from(approvalDocument);
    }

    //기안 내용 수정 (DRAFT 상태에서만)
    @Transactional
    public void updateApprovalDocument(UpdateApprovalDocumentRequest request, Long targetId, Long drafterId){

        ApprovalDocument approvalDocument = approvalDocumentRepository.findById(targetId).orElseThrow(()-> new IllegalArgumentException(("존재하지 않는 결재입니다.")));

        if(!approvalDocument.getDrafter().getId().equals(drafterId)){
            throw new AccessDeniedException("기안 수정 권한이 없습니다.");
        }

        if(approvalDocument.getStatus() != DocumentStatus.DRAFT){
            throw new IllegalStateException("임시저장 상태에서만 수정할 수 있습니다.");
        }

        approvalDocument.updateApprovalDocument(request.docType(), request.title(), request.content());
    }

}
