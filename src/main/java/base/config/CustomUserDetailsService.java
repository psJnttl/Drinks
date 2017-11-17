package base.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import base.domain.Account;
import base.repository.AccountRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (null == account) {
            throw new UsernameNotFoundException("No such user: " + username);
        }
        User user = new User(account.getUsername(), account.getPassword(), true, true, true, true,
                getRoles(account));
        return user;
    }
    
    private List<GrantedAuthority> getRoles(Account account) {
        return account.getRoles().stream()
            .map(e ->  e.getName())
            .map(n -> new SimpleGrantedAuthority(n))
            .collect(Collectors.toList());
    }

}
