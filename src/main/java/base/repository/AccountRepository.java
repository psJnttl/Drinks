package base.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import base.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
}
