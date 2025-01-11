package account.accountproject.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("사용자가 없습니다."),
    MAX_ACCOUNT_PER_USER_10("한 사람의 최대 계좌 개수는 10개입니다."),
    ACCOUNT_NOT_FOUND("계좌가 없습니다."),
    USER_ACCOUNT_UN_MATCH("사용자와 계좌의 소유주가 다릅니다."),
    ACCOUNT_ALREADY_UNREGISTERED("계좌가 이미 해지상태입니다."),
    BALANCE_NOT_EMPTY("계좌에 잔액이 있습니다."),
    AMOUNT_EXCEED_BALANCE("계좌 잔액이 사용 금액보다 적습니다.");

    private final String description;
}
