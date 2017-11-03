package base.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import base.domain.Account;
import base.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Optional<AccountDto> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            return Optional.empty();
        }
        System.out.println("authentication.getName(): " + authentication.getName());
        Account account = accountRepository.findByUsername(authentication.getName());
        AccountDto dto = new AccountDto(account.getUsername());
        dto.setRoles(account.getRoles());
        return Optional.of(dto);
    }

}
