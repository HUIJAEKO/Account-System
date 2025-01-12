package account.accountproject.service;

import account.accountproject.domain.Account;
import account.accountproject.domain.AccountUser;
import account.accountproject.domain.Transaction;
import account.accountproject.dto.TransactionDto;
import account.accountproject.exception.AccountException;
import account.accountproject.repository.AccountRepository;
import account.accountproject.repository.AccountUserRepository;
import account.accountproject.repository.TransactionalRepository;
import account.accountproject.type.AccountStatus;
import account.accountproject.type.ErrorCode;
import account.accountproject.type.TransactionType;
import account.accountproject.type.TransactionalResultType;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static account.accountproject.type.TransactionType.CANCEL;
import static account.accountproject.type.TransactionType.USE;
import static account.accountproject.type.TransactionalResultType.F;
import static account.accountproject.type.TransactionalResultType.S;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionalRepository transactionalRepository;
    private final AccountUserRepository accountUserRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public TransactionDto useBalance(Long userId, String accountNumber, Long amount){
        AccountUser accountUser = accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));
        Account account = accountRepository.findByAccountNumber(accountNumber)
                 .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));


        validateUseBalance(accountUser, account, amount);

        account.useBalance(amount);

        return TransactionDto.fromEntity(
                saveAndGetTransaction(USE, S, amount, account));
    }

    private void validateUseBalance(AccountUser accountUser, Account account, Long amount) {
        if(!Objects.equals(accountUser.getId(), account.getAccountUser().getId())){
            throw new AccountException(ErrorCode.USER_ACCOUNT_UN_MATCH);
        }

        if(account.getAccountStatus() != AccountStatus.IN_USE){
            throw new AccountException(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED);
        }

        if(account.getBalance() < amount){
            throw new AccountException(ErrorCode.AMOUNT_EXCEED_BALANCE);
        }
    }


    @Transactional
    public void saveFailedUseTransaction(String accountNumber, Long amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        saveAndGetTransaction(USE, F, amount, account);
    }

    private Transaction saveAndGetTransaction(TransactionType transactionType, TransactionalResultType transactionResultType, Long amount, Account account) {
        return transactionalRepository.save(
                Transaction.builder()
                        .transactionType(transactionType)
                        .transactionResultType(transactionResultType)
                        .account(account)
                        .amount(amount)
                        .balancedSnapshot(Long.valueOf(UUID.randomUUID().toString().replace("-", "")))
                        .transactedAt(LocalDateTime.now())
                        .build()
        );
    }

    @Transactional
    public TransactionDto CancelBalance(
            String transactionId, String accountNumber, Long amount) {
        Transaction transaction = transactionalRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new AccountException(ErrorCode.TRANSACTION_NOT_FOUND));
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        validateCancelBalance(transaction, account, amount);

        account.cancelBalance(amount);

        return TransactionDto.fromEntity(
                saveAndGetTransaction(CANCEL, S, amount, account));
    }

    private void validateCancelBalance(Transaction transaction, Account account, Long amount) {
        if(!Objects.equals(transaction.getAccount().getId(), account.getId())){
            throw new AccountException(ErrorCode.TRANSACTION_ACCOUNT_NOT_FOUND);
        }

        if(!Objects.equals(transaction.getAmount(), amount)){
            throw new AccountException(ErrorCode.CANCEL_MUST_FULLY);
        }

        if(transaction.getTransactedAt().isBefore(LocalDateTime.now().minusYears(1))){
            throw new AccountException(ErrorCode.TOO_OLD_ORDER_TO_CANCEL);
        }
    }

    @Transactional
    public void saveFailedCancelTransaction(@NotBlank @Size(min=10, max=10) String accountNumber, @NotNull @Min(10) @Max(100_000_000) Long amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        saveAndGetTransaction(CANCEL, F, amount, account);
    }
}
