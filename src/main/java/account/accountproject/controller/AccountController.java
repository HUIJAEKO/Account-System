package account.accountproject.controller;

import account.accountproject.domain.Account;
import account.accountproject.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/create-account")
    public String createAccount(){
        accountService.createAccount();
        return "Success";
    }

    @GetMapping("/account/{id}")
    public Account getAccount(Long id){
        return accountService.getAccount(id);
    }
}
