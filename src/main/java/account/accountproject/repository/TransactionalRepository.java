package account.accountproject.repository;

import account.accountproject.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionalRepository extends JpaRepository<Transaction, Long> {
}
