package base.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

public class CsrfHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter)
            throws ServletException, IOException {
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (null != csrf) {
            response.addHeader("X-CSRF-HEADER", csrf.getHeaderName());
            response.addHeader("X-CSRF-PARAM", csrf.getParameterName());
            response.addHeader("X-CSRF-TOKEN", csrf.getToken());
        }
        filter.doFilter(request, response);
    }

}
