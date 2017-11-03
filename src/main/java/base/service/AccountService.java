package base.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import base.controller.AccountAdd;
import base.domain.Account;
import base.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<AccountDto> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            return Optional.empty();
        }
        Account account = accountRepository.findByUsername(authentication.getName());
        AccountDto dto = new AccountDto(account.getUsername());
        dto.setRoles(account.getRoles());
        return Optional.of(dto);
    }

    public Optional<AccountDto> addUser(AccountAdd account) {
        Account existing = accountRepository.findByUsername(account.getUsername());
        if (null != existing) {
            return Optional.empty();
        }
        Account user = new Account();
        user.setUsername(account.getUsername());
        user.setPassword(passwordEncoder.encode(account.getPassword()));
        user.setRoles(account.getRoles());
        user = accountRepository.saveAndFlush(user);
        AccountDto dto = new AccountDto(user.getUsername());
        dto.setRoles(user.getRoles());
        return Optional.of(dto);
    }

}
