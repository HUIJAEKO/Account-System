package account.accountproject.dto;

import account.accountproject.type.TransactionType;
import account.accountproject.type.TransactionalResultType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryTransactionResponse {
    private String accountNumber;
    private TransactionType transactionType;
    private TransactionalResultType transactionResultType;
    private Long amount;
    private String transactionId;
    private LocalDateTime transactedAt;

    public static TransactionDto fromEntity(TransactionDto transaction){
        return TransactionDto.builder()
                .accountNumber(transaction.getAccount().getAccountNumber())
                .transactionType(transaction.getTransactionType())
                .transactionResultType(transaction.getTransactionResultType())
                .amount(transaction.getAmount())
                .transactionId(transaction.getTransactionId())
                .transactedAt(transaction.getTransactedAt())
                .build();
    }
}
