package account.accountproject.controller;

import account.accountproject.dto.CancelBalance;
import account.accountproject.dto.UseBalance;
import account.accountproject.exception.AccountException;
import account.accountproject.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transaction/use")
    public UseBalance.Response useBalance(
            @Valid @RequestBody UseBalance.Request request
    ){
        try{
            UseBalance.Response.from(transactionService.useBalance(
                request.getUserId(), request.getAccountNumber(), request.getAmount())
            );
        }catch(AccountException e){
            log.error("Failed to use balance");

            transactionService.saveFailedUseTransaction(
                request.getAccountNumber(),
                request.getAmount()
            );

            throw e;
        }
        return null;
    }

    @DeleteMapping("/transaction/cancel")
    public CancelBalance.Response useBalance(
            @Valid @RequestBody CancelBalance.Request request
    ){
            try{
                UseBalance.Response.from(transactionService.CancelBalance(
                        request.getTransactionId(), request.getAccountNumber(), request.getAmount())
            );
        }catch(AccountException e){
            log.error("Failed to cancel balance");

            transactionService.saveFailedCancelTransaction(
                    request.getAccountNumber(),
                    request.getAmount()
            );

            throw e;
        }
        return null;
    }
}
