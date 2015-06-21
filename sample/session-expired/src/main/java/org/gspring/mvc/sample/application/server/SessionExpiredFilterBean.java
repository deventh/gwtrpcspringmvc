package org.gspring.mvc.sample.application.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.gspring.mvc.rpc.RpcGwtHandlerMapping;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service("sessionExpiredFilter")
public class SessionExpiredFilterBean extends GenericFilterBean {
    @Resource
    private RpcGwtHandlerMapping rpcGwtHandlerMapping;
    @Resource
    private Advisor sessionExpiredExceptionAdvisor;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        if (!httpServletRequest.getRequestURI().endsWith(".gwt")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (sessionIsExpired(servletRequest)) {
            handleExpiredSession(httpServletRequest, httpServletResponse);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void handleExpiredSession(HttpServletRequest httpServletRequest, HttpServletResponse servletResponse) throws ServletException {
        try {
            // use caching here (CacheManager),
            // creating the proxy on the fly takes some time
            Object handler = rpcGwtHandlerMapping.lookupHandler(httpServletRequest.getRequestURI(), httpServletRequest);

            ProxyFactory pf = new ProxyFactory();
            pf.setProxyTargetClass(true);
            pf.setTarget(handler);
            pf.addAdvisor(sessionExpiredExceptionAdvisor);
            pf.setFrozen(true);

            RemoteServiceServlet rpcServlet = new RemoteServiceServlet(pf.getProxy()) {
                @Override
                public ServletContext getServletContext() {
                    return SessionExpiredFilterBean.this.getServletContext();
                }

                @Override
                public String getServletName() {
                    return RemoteServiceServlet.class.getName();
                }
            };

            rpcServlet.doPost(httpServletRequest, servletResponse);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private boolean sessionIsExpired(ServletRequest servletRequest) {
        // default key value, inject here if overridden
        String key = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

        return ((HttpServletRequest) servletRequest).getSession().getAttribute(key) == null;
    }


}
