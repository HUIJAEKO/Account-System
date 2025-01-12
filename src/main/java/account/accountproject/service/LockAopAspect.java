package account.accountproject.service;

import account.accountproject.aop.AccountLockIdInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LockAopAspect {
    private final LockService lockService;

    @Around("@annotation(account.accountproject.aop.AccountLock) && args(request)")
    public Object arountdethod(ProceedingJoinPoint pjp, AccountLockIdInterface request) throws Throwable{
        lockService.lock(request.getAccountNumber());
        try {
            return pjp.proceed();
        }finally{
            lockService.unlock(request.getAccountNumber());
        }
    }
}
