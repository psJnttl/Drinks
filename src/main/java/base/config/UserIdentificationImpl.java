package base.config;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import base.domain.Role;

@Component
public class UserIdentificationImpl implements UserIdentification {

    @Override
    public Authentication getAuthentication() {
        return getAuth();
    }
    
    @Override
    public boolean isUserLogged() {
        return null != getAuth();
    }

    @Override
    public String getName() {
        Authentication authentication = getAuth();
        return authentication.getName();
    }

    @Override
    public boolean isOAuth2() {
        Authentication authentication = getAuth();
        return (authentication instanceof OAuth2Authentication);
    }

    @Override
    public List<Role> getRoles() {
        Collection <? extends GrantedAuthority> authorities = SecurityContextHolder
                                                                  .getContext()
                                                                  .getAuthentication()
                                                                  .getAuthorities();
        return authorities.stream()
                          .map(a -> new Role( a.getAuthority()))
                          .collect(Collectors.toList());
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String getNameForLog() {
        String name = getName();
        if (isOAuth2()) {
            name += "+oa2";
        }
        return name;
    }

    
}
