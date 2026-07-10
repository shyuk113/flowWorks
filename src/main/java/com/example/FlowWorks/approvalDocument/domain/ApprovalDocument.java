package com.example.FlowWorks.approvalDocument.domain;

import com.example.FlowWorks.employee.domain.Employee;
import com.example.FlowWorks.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
}
