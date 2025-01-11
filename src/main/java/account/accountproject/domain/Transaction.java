package account.accountproject.domain;

import account.accountproject.type.TransactionType;
import account.accountproject.type.TransactionalResultType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionalResultType transactionResultType;

    @ManyToOne
    private Account account;
    private Long amount;
    private Long balancedSnapshot;

    private String transactionId;
    private LocalDateTime transactedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
