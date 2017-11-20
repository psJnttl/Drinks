package base.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import base.command.AccountAdd;
import base.command.AccountMod;
import base.domain.Account;
import base.dto.AccountDto;
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

    @Transactional
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

    public boolean isRequestValid(AccountMod account) {
        if (null == account || null == account.getOldPassword() || null == account.getNewPassword() ||
                null == account.getRoles() || null == account.getUsername()) {
            return false;
        }
        if (account.getOldPassword().isEmpty() || account.getNewPassword().isEmpty() || account.getRoles().isEmpty() ||
                account.getUsername().isEmpty()) {
            return false;
        }
        Optional<AccountDto> user = getUser();
        if (!user.isPresent()) {
            return false;
        }
        if (!user.get().getUsername().equals(account.getUsername())) {
            return false;
        }
        return true;
    }

    /**
     * Used for changing password. Can't be used for changing user roles.
     * Please use method isRequestValid to validate input first.
     * @param accountMod  command on account to be changed
     * @return AccountDto  Optional.empty if old password was incorrect
     *                     If password change OK, has username & roles, but no password.
     */
    @Transactional
    public Optional<AccountDto> changePassword(AccountMod account) {
        Optional<AccountDto> user = getUser();
        if (!user.isPresent()) {
            return Optional.empty();
        }
        Account accountToMod = accountRepository.findByUsername(user.get().getUsername());
        if (!passwordEncoder.matches(account.getOldPassword(), accountToMod.getPassword()) ) {
            return Optional.empty();
        }
        accountToMod.setPassword(passwordEncoder.encode(account.getNewPassword()));
        accountToMod = accountRepository.saveAndFlush(accountToMod);
        AccountDto dto = new AccountDto(accountToMod.getUsername());
        dto.setRoles(accountToMod.getRoles());
        return Optional.of(dto);
    }

}
