package base.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import base.command.AccountAdd;
import base.command.AccountMod;
import base.config.UserIdentification;
import base.domain.Account;
import base.domain.Role;
import base.dto.AccountDto;
import base.dto.RoleDto;
import base.repository.AccountRepository;
import base.repository.RoleRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserIdentification userIdentification;

    public Optional<AccountDto> getUser() {
        if (!userIdentification.isUserLogged()) {
            return Optional.empty();
        }
        if (!userIdentification.isOAuth2()) {
            Account account = accountRepository.findByUsername(userIdentification.getName());
            if (null != account) {
                AccountDto dto = createDto(account);
                return Optional.of(dto);
            }
            return Optional.empty();
        }
        else {
            List<Role> roles = userIdentification.getRoles();
            if (roles.isEmpty()) {
                return Optional.empty();
            }
            AccountDto dto = new AccountDto(-1, userIdentification.getName(), roles);
            return Optional.of(dto);
        }

    }

    /**
     * Add user during signup. Authorization level is always USER.
     * 
     * @param accountAdd
     *            command
     * @return Optional AccountDto to be returned to client.
     */

    @Transactional
    public Optional<AccountDto> addUser(AccountAdd account) {
        Account user = new Account();
        user.setUsername(account.getUsername());
        user.setPassword(passwordEncoder.encode(account.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(Arrays.asList(userRole));
        user = accountRepository.saveAndFlush(user);
        AccountDto dto = createDto(user);
        return Optional.of(dto);
    }

    public boolean doesAccountExist(AccountAdd account) {
        Account existing = accountRepository.findByUsername(account.getUsername());
        if (null != existing) {
            return true;
        }
        return false;
    }
    
    public Optional<AccountDto> findAccount(long id) {
        Account account = accountRepository.findOne(id);
        if (null == account) {
            return Optional.empty();
        }
        AccountDto dto = new AccountDto(account.getUsername(), account.getRoles());
        return Optional.of(dto);
    }

    public boolean isRequestValid(AccountMod account) {
        if (null == account || null == account.getOldPassword() || null == account.getNewPassword()
                || null == account.getRoles() || null == account.getUsername()) {
            return false;
        }
        if (account.getOldPassword().isEmpty() || account.getNewPassword().isEmpty() || account.getRoles().isEmpty()
                || account.getUsername().isEmpty()) {
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
     * Used for changing password. Can't be used for changing user roles. Please
     * use method isRequestValid to validate input first.
     * 
     * @param accountMod
     *            command on account to be changed
     * @return AccountDto Optional.empty if old password was incorrect If
     *         password change OK, has username & roles, but no password.
     */
    @Transactional
    public Optional<AccountDto> changePassword(AccountMod account) {
        Optional<AccountDto> user = getUser();
        if (!user.isPresent()) {
            return Optional.empty();
        }
        Account accountToMod = accountRepository.findByUsername(user.get().getUsername());
        if (!passwordEncoder.matches(account.getOldPassword(), accountToMod.getPassword())) {
            return Optional.empty();
        }
        accountToMod.setPassword(passwordEncoder.encode(account.getNewPassword()));
        accountToMod = accountRepository.saveAndFlush(accountToMod);
        AccountDto dto = createDto(accountToMod);
        return Optional.of(dto);
    }

    @Transactional(readOnly=true)
    public List<AccountDto> listAll() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(a -> createDto(a) ).collect(Collectors.toList());
    }

    @Transactional
    public AccountDto addUserWithAuthorization(AccountAdd account) {
        Account user = new Account();
        user.setUsername(account.getUsername());
        user.setPassword(passwordEncoder.encode(account.getPassword()));
        user.setRoles(getRoleEntities(account.getRoles()));
        user = accountRepository.saveAndFlush(user);
        AccountDto dto = createDto(user);
        return dto;
    }

    private List<Role> getRoleEntities(List<Role> roles) {
        return roles.stream()
                    .map(r -> roleRepository.findByName(r.getName()))
                    .collect(Collectors.toList());
    }
    /**
     * Modify active account: set authorization roles or reset password.
     * Password is reset if new password is defined. Roles are changed if they're defined.
     * @param AccountMod
     * @return AccountDto
     */
    @Transactional
    public AccountDto modifyAccount(AccountMod account) {
        Account user = accountRepository.findByUsername(account.getUsername());
        if (null != account.getNewPassword() && !account.getNewPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(account.getNewPassword()));
        }
        if (null != account.getRoles() && !account.getRoles().isEmpty()) {
            user.setRoles(getRoleEntities(account.getRoles()));
        }
        user = accountRepository.saveAndFlush(user);
        AccountDto dto = createDto(user);
        return dto;
    }

    @Transactional
    public boolean deleteAccountByUsername(String username) {
        Account account = accountRepository.findByUsername(username);
        if (null == account) {
            return false;
        }
        accountRepository.delete(account);
        return true;
    }
    
    @Transactional
    public boolean deleteAccount(long id) {
        Account account = accountRepository.findOne(id);
        if (null == account) {
            return false;
        }
        accountRepository.delete(account);
        return true;

    }

    private AccountDto createDto(Account account) {
        AccountDto dto = new AccountDto(account.getId(), account.getUsername(), account.getRoles());
        return dto;
    }
    
    public boolean isSignupValid(AccountAdd account) {
        if (null == account || null == account.getUsername() || null == account.getPassword() || 
            null == account.getRoles() || account.getUsername().isEmpty() || 
            account.getPassword().isEmpty() || account.getRoles().isEmpty()) {
                return false;
        }
        return true;
    }

}
