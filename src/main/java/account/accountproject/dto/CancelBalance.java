package account.accountproject.dto;

import account.accountproject.type.TransactionalResultType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CancelBalance {

    @Data
    public static class Request{
        @NotBlank
        private String transactionId;

        @NotBlank
        @Size(min=10, max=10)
        private String accountNumber;

        @NotNull
        @Min(10)
        @Max(100_000_000)
        private Long amount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response{
        private String accountNumber;
        private TransactionalResultType transactionalResultType;
        private String transactionId;
        private Long amount;
        private LocalDateTime transactedAt;

        public static Response from(TransactionDto transactionDto) {
            return Response.builder()
                    .accountNumber(transactionDto.getAccountNumber())
                    .transactionalResultType(transactionDto.getTransactionResultType())
                    .transactionId(transactionDto.getTransactionId())
                    .amount(transactionDto.getAmount())
                    .transactedAt(transactionDto.getTransactedAt())
                    .build();
        }
    }
}
