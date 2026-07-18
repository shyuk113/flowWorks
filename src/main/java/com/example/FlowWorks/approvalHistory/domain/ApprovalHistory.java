package com.example.FlowWorks.approvalHistory.domain;

import com.example.FlowWorks.approvalDocument.domain.ApprovalDocument;
import com.example.FlowWorks.approvalStep.domain.ApprovalStep;
import com.example.FlowWorks.employee.domain.Employee;
import com.example.FlowWorks.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApprovalHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private ApprovalDocument approvalDocument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id", nullable = false)
    private ApprovalStep approvalStep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id",  nullable = false)
    private Employee actor;

    @Enumerated(EnumType.STRING)
    private Action action;

    private String comment;

    @Builder
    private ApprovalHistory(ApprovalDocument approvalDocument, ApprovalStep approvalStep, Employee actor, Action action, String comment) {
        this.approvalDocument = approvalDocument;
        this.approvalStep = approvalStep;
        this.actor = actor;
        this.action = action;
        this.comment = comment;
    }

    public static ApprovalHistory createHistory(ApprovalDocument approvalDocument, ApprovalStep approvalStep, Employee actor, Action action, String comment) {
        return ApprovalHistory.builder()
                .approvalDocument(approvalDocument)
                .approvalStep(approvalStep)
                .actor(actor)
                .action(action)
                .comment(comment)
                .build();
    }

}
