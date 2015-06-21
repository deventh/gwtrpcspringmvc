package org.gspring.mvc.sample.application.server;

import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service("sessionExpiredFilter")
public class SessionExpiredFilterBean extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        if (!httpServletRequest.getRequestURI().endsWith(".gwt")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (sessionIsExpired(servletRequest)) {
            handleExpiredSession(httpServletResponse);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void handleExpiredSession(HttpServletResponse servletResponse) throws ServletException, IOException {
        servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Relogin required");
    }

    private boolean sessionIsExpired(ServletRequest servletRequest) {
        // default key value, inject here if overridden
        String key = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

        return ((HttpServletRequest) servletRequest).getSession().getAttribute(key) == null;
    }


}
