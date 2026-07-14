package com.example.FlowWorks.approvalDocument.domain;

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
public class ApprovalDocument extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DocType docType;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drafter_id")
    private Employee drafter;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    private int currentRound;

    @Builder
    private ApprovalDocument(DocType docType, String title, String content, Employee drafter, int currentRound, DocumentStatus status) {
        this.docType = docType;
        this.title = title;
        this.content = content;
        this.drafter = drafter;
        this.currentRound = currentRound;
        this.status = status;
    }

    public static ApprovalDocument createApprovalDocument(DocType docType, String title, String content, Employee drafter) {
        return ApprovalDocument.builder()
                .docType(docType)
                .title(title)
                .content(content)
                .drafter(drafter)
                .currentRound(0)
                .status(DocumentStatus.DRAFT)
                .build();
    }

    public void updateApprovalDocument(DocType docType, String title, String content) {
        this.docType = docType;
        this.title = title;
        this.content = content;
    }

    public void updateApprovalDocumentStatus(DocumentStatus status) {
        this.status = status;
    }

}
