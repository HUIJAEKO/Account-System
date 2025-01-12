package account.accountproject.service;

import account.accountproject.exception.AccountException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static account.accountproject.type.ErrorCode.ACCOUNT_TRANSACTION_LOCK;

@Service
@Slf4j
@RequiredArgsConstructor
public class LockService {
    private final RedissonClient redissonClient;

    public void lock(String accountNumber){
        RLock lock = redissonClient.getLock("ACLK" + accountNumber);

        try{
            boolean isLock = lock.tryLock(1,5, TimeUnit.SECONDS);
            if(!isLock){
                log.error("lock failed");
                throw new AccountException(ACCOUNT_TRANSACTION_LOCK);
            }
        }catch(Exception e){
            log.error("redis lock failed");
        }
    }

    public void unlock(String accountNumber){
        redissonClient.getLock("ACLK" + accountNumber).unlock();
    }
}
