package account.accountproject.dto;

import account.accountproject.domain.Account;
import account.accountproject.domain.Transaction;
import account.accountproject.type.TransactionType;
import account.accountproject.type.TransactionalResultType;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {
    private String accountNumber;
    private TransactionType transactionType;
    private TransactionalResultType transactionResultType;
    private Long amount;
    private Long balancedSnapshot;
    private String transactionId;
    private LocalDateTime transactedAt;

    public static TransactionDto fromEntity(Transaction transaction){
        return TransactionDto.builder()
                .accountNumber(transaction.getAccount().getAccountNumber())
                .transactionType(transaction.getTransactionType())
                .transactionResultType(transaction.getTransactionResultType())
                .amount(transaction.getAmount())
                .balancedSnapshot(transaction.getBalancedSnapshot())
                .transactionId(transaction.getTransactionId())
                .transactedAt(transaction.getTransactedAt())
                .build();
    }
}
