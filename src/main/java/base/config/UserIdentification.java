package base.config;

import java.util.List;

import org.springframework.security.core.Authentication;

import base.domain.Role;

public interface UserIdentification {
    Authentication getAuthentication();
    boolean isUserLogged();
    String getName();
    boolean isOAuth2();
    List<Role> getRoles();
}
