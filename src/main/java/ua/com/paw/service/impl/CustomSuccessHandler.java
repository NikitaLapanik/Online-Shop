package ua.com.paw.service.impl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import ua.com.paw.entity.enums.Role;

import java.io.IOException;
import java.util.Collection;

@Service
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals(Role.ROLE_ADMIN.name()))) {
            response.sendRedirect("/");
        } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals(Role.ROLE_USER.name()))) {
            response.sendRedirect("/");
        } else {
            response.sendRedirect("/error");
        }
    }
}
