package com.github.knoweth.server.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This component essentially just redirects to /#/home after logging in. This
 * is necessary because otherwise Spring tries to redirect you to the last
 * authenticated endpoint, which is usually the status endpoint telling you
 * whether or not you've logged in. For an API-esque server, this isn't very
 * useful.
 */
@Component
public class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse arg1, Authentication authentication)
            throws IOException {
        redirectStrategy.sendRedirect(arg0, arg1, "/#/home");
    }
}