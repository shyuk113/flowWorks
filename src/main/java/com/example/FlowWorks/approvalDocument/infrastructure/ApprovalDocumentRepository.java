package com.example.FlowWorks.approvalDocument.infrastructure;

import com.example.FlowWorks.approvalDocument.domain.ApprovalDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalDocumentRepository extends JpaRepository<ApprovalDocument,Long> {
}
