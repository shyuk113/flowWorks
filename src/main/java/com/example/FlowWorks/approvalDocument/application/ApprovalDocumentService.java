package com.example.FlowWorks.approvalDocument.application;

import com.example.FlowWorks.approvalDocument.application.dto.ApprovalDocumentResponse;
import com.example.FlowWorks.approvalDocument.application.dto.CreateApprovalDocumentRequest;
import com.example.FlowWorks.approvalDocument.application.dto.UpdateApprovalDocumentRequest;
import com.example.FlowWorks.approvalDocument.domain.ApprovalDocument;
import com.example.FlowWorks.approvalDocument.domain.DocumentStatus;
import com.example.FlowWorks.approvalDocument.infrastructure.ApprovalDocumentRepository;
import com.example.FlowWorks.approvalHistory.domain.Action;
import com.example.FlowWorks.approvalHistory.domain.ApprovalHistory;
import com.example.FlowWorks.approvalHistory.infrastructure.ApprovalHistoryRepository;
import com.example.FlowWorks.approvalStep.domain.ApprovalStep;
import com.example.FlowWorks.approvalStep.domain.ApprovalStepStatus;
import com.example.FlowWorks.approvalStep.domain.StepType;
import com.example.FlowWorks.approvalStep.infrastructure.ApprovalStepRepository;
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
    private final ApprovalStepRepository  approvalStepRepository;

    private static final int MANAGER_MIN_RANK = 5; //임시 설정
    private final ApprovalHistoryRepository approvalHistoryRepository;

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

        List<ApprovalStep> steps = approvalStepRepository.findByApprovalDocumentIdAndRoundNumber(approvalDocument.getId(), approvalDocument.getCurrentRound());

        return ApprovalDocumentResponse.withSteps(approvalDocument, steps);
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

    //기안 상신
    @Transactional
    public void submit(Long id, Long drafterId){

        ApprovalDocument approvalDocument = approvalDocumentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException(("존재하지 않는 결재입니다.")));

        Employee drafter = employeeRepository.findById(drafterId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 직원입니다."));

        if(!approvalDocument.getDrafter().getId().equals(drafter.getId())){
            throw new AccessDeniedException("본인의 기안만 상신할 수 있습니다.");
        }

        if(approvalDocument.getStatus() != DocumentStatus.DRAFT){
            throw new IllegalStateException("DRAFT상태의 기안만 상신할 수 있습니다.");
        }

        Employee teamLeader = drafter.getTeam().getTeamLeader();
        Employee departmentHead = drafter.getTeam().getDepartment().getDepartmentHead();

        if(teamLeader == null){
            throw new IllegalStateException("팀장이 지정되지 않아 상신할 수 없습니다.");
        }
        if(departmentHead == null){
            throw new IllegalStateException("부서장이 지정되지 않아 상신할 수 없습니다.");
        }

        approvalDocument.updateApprovalDocumentStatus(DocumentStatus.IN_PROGRESS);

        approvalDocument.updateCurrentRound(approvalDocument.getCurrentRound()+1);

        ApprovalStep step1 = ApprovalStep.createStep(approvalDocument, approvalDocument.getCurrentRound(), 1, StepType.TEAM_LEADER, teamLeader);
        ApprovalStep step2 = ApprovalStep.createStep(approvalDocument, approvalDocument.getCurrentRound(), 2, StepType.DEPT_HEAD, departmentHead);

        approvalStepRepository.save(step1);
        approvalStepRepository.save(step2);

        ApprovalHistory history = ApprovalHistory.createHistory(approvalDocument,step1, drafter, Action.SUBMIT,null);

        approvalHistoryRepository.save(history);
    }

    @Transactional
    public void approve(Long id, Long stepId, Long employeeId){
        ApprovalDocument approvalDocument = approvalDocumentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 기안입니다."));
        ApprovalStep step = approvalStepRepository.findById(stepId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 단계입니다."));

        if(step.getApprover() == null){
            throw new IllegalStateException("승인자가 지정되지 않았습니다.");
        }

        if(approvalDocument.getStatus() != DocumentStatus.IN_PROGRESS){
            throw new IllegalStateException("진행중인 결재만 승인할 수 있습니다.");
        }

        if(step.getStatus() != ApprovalStepStatus.PENDING){
            throw new IllegalStateException("이미 처리된 단계입니다.");
        }

        if(!step.getApprover().getId().equals(employeeId)){
            throw new AccessDeniedException("본인에게 배정된 결재만 승인할 수 있습니다.");
        }

        List<ApprovalStep> roundSteps = approvalStepRepository.findByApprovalDocumentIdAndRoundNumber(id, approvalDocument.getCurrentRound());
        boolean previousNotApproved = roundSteps.stream().anyMatch(
                s -> s.getStepOrder() < step.getStepOrder() && s.getStatus() != ApprovalStepStatus.APPROVED
        );

        if(previousNotApproved){
            throw new IllegalStateException("이전단계가 아직 승인되지 않았습니다.");
        }

        step.approve();

        boolean allApproved = roundSteps.stream().allMatch(
                s->s.getId().equals(step.getId()) || s.getStatus() == ApprovalStepStatus.APPROVED
        );

        if(allApproved){
            approvalDocument.updateApprovalDocumentStatus(DocumentStatus.APPROVED);
        }

        approvalHistoryRepository.save(ApprovalHistory.createHistory(approvalDocument,step,step.getApprover(),Action.APPROVE,null));
    }

    @Transactional
    public void reject(Long id, Long stepId, Long employeeId, String comment){
        ApprovalDocument approvalDocument = approvalDocumentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 기안입니다."));
        ApprovalStep step = approvalStepRepository.findById(stepId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 단계입니다."));

        if(step.getApprover() == null) {
            throw new IllegalStateException("승인자가 지정되지 않았습니다.");
        }

        if(approvalDocument.getStatus() != DocumentStatus.IN_PROGRESS){
            throw new IllegalStateException("진행중인 결재만 반려할 수 있습니다.");
        }

        if(step.getStatus() != ApprovalStepStatus.PENDING){
            throw new IllegalStateException("이미 처리된 단계입니다.");
        }

        if(!step.getApprover().getId().equals(employeeId)){
            throw new AccessDeniedException("본인에게 배정된 결재만 반려할 수 있습니다.");
        }

        List<ApprovalStep> roundSteps = approvalStepRepository.findByApprovalDocumentIdAndRoundNumber(id, approvalDocument.getCurrentRound());
        boolean previousNotApproved = roundSteps.stream().anyMatch(
                s -> s.getStepOrder() < step.getStepOrder() && s.getStatus() != ApprovalStepStatus.APPROVED
        );

        if(previousNotApproved){
            throw new IllegalStateException("이전단계가 아직 처리되지 않았습니다.");
        }

        step.reject(comment);

        approvalDocument.updateApprovalDocumentStatus(DocumentStatus.REJECTED);

        approvalHistoryRepository.save(ApprovalHistory.createHistory(approvalDocument,step,step.getApprover(),Action.REJECT,comment));
    }
}
