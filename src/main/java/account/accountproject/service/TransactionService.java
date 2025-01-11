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
import account.accountproject.type.TransactionalResultType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

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
                saveAndGetTransaction(S, amount, account));
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

        saveAndGetTransaction(F, amount, account);
    }

    private Transaction saveAndGetTransaction(TransactionalResultType transactionResultType, Long amount, Account account) {
        return transactionalRepository.save(
                Transaction.builder()
                        .transactionType(USE)
                        .transactionResultType(transactionResultType)
                        .account(account)
                        .amount(amount)
                        .balancedSnapshot(Long.valueOf(UUID.randomUUID().toString().replace("-", "")))
                        .transactedAt(LocalDateTime.now())
                        .build()
        );
    }
}
