package account.accountproject.domain;

import account.accountproject.exception.AccountException;
import account.accountproject.type.AccountStatus;
import account.accountproject.type.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private AccountUser accountUser;

    private String accountNumber;

    private Long balance;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    private LocalDateTime registeredAt;
    private LocalDateTime unRegisteredAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void useBalance(Long amount){
        if(amount > balance){
            throw new AccountException(ErrorCode.AMOUNT_EXCEED_BALANCE);
        }
        balance -= amount;
    }
}
