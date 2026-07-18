package com.example.FlowWorks.approvalStep.domain;

import com.example.FlowWorks.approvalDocument.domain.ApprovalDocument;
import com.example.FlowWorks.employee.domain.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"document_id","round_number","step_order"}))
public class ApprovalStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private ApprovalDocument approvalDocument;

    private int roundNumber;

    private int stepOrder;

    @Enumerated(EnumType.STRING)
    private StepType stepType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private Employee approver;

    @Enumerated(EnumType.STRING)
    private ApprovalStepStatus status;

    private LocalDateTime approvedAt;

    private String comment;

    @Builder
    private ApprovalStep(ApprovalDocument approvalDocument, int roundNumber, int stepOrder, StepType stepType, Employee approver, ApprovalStepStatus status, String comment) {
        this.approvalDocument = approvalDocument;
        this.roundNumber = roundNumber;
        this.stepOrder = stepOrder;
        this.stepType = stepType;
        this.approver = approver;
        this.status = status;
        this.comment = comment;
    }

    public static ApprovalStep createStep(ApprovalDocument approvalDocument, int roundNumber, int stepOrder, StepType stepType, Employee approver){
        return ApprovalStep.builder()
                .approvalDocument(approvalDocument)
                .roundNumber(roundNumber)
                .stepOrder(stepOrder)
                .stepType(stepType)
                .approver(approver)
                .status(ApprovalStepStatus.PENDING)
                .build();
    }

}
