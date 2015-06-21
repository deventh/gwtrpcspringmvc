package org.gspring.mvc.rf;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class RfGwtHandlerAdapter implements HandlerAdapter {
    private static final int NEVER_LAST_MODIFIED = -1;

    @Resource
    private View rfGwtView;

    @Override
    public boolean supports(Object handler) {
        return handler instanceof RfGwtHandlerMapping.RfHandlerMarker;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return new ModelAndView(rfGwtView);
    }

    @Override
    public long getLastModified(HttpServletRequest request, Object handler) {
        return NEVER_LAST_MODIFIED;
    }

}
