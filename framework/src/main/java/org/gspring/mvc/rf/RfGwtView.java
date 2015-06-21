package org.gspring.mvc.rf;

import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.View;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
class RfGwtView implements View, ServletContextAware, ApplicationContextAware {
    private final Log logger = LogFactory.getLog(getClass());

    private RfServlet requestFactoryServlet;
    private ApplicationContext context;

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        requestFactoryServlet.render(request, response);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        // this method is invoked once before any 'init' methods, so let's do lazy initialization here
        requestFactoryServlet = createServlet();

        requestFactoryServlet.setServletContext(servletContext);
    }

    private RfServlet createServlet() {
        if (foundExceptionHandlerOrServiceLayerDecorators()) {
            return new RfServlet(findExceptionHandler(), findServiceLayerDecorators());
        } else {
            return new RfServlet();
        }
    }

    private boolean foundExceptionHandlerOrServiceLayerDecorators() {
        return findServiceLayerDecorators() != null || findServiceLayerDecorators() != null;
    }

    private ServiceLayerDecorator[] findServiceLayerDecorators() {
        Map<String, ServiceLayerDecorator> matchingBeans = beansOfTypeIncludingAncestors(context, ServiceLayerDecorator.class);
        if (!matchingBeans.isEmpty()) {
            return matchingBeans.values().toArray(new ServiceLayerDecorator[0]);
        }
        return null;
    }

    private ExceptionHandler findExceptionHandler() {
        Map<String, ExceptionHandler> matchingBeans = beansOfTypeIncludingAncestors(context, ExceptionHandler.class);
        if (!matchingBeans.isEmpty()) {
            List<ExceptionHandler> handlers = new ArrayList<ExceptionHandler>(matchingBeans.values());
            if (handlers.size() > 1) {
                OrderComparator.sort(handlers);
                logger.info("Found more than one beans of type: " + ExceptionHandler.class + ", choosing first: " + handlers.get(0));
            }
            return handlers.get(0);
        }
        return null;
    }

    private static <T> Map<String, T> beansOfTypeIncludingAncestors(ApplicationContext context, Class<T> type) {
        return BeanFactoryUtils.beansOfTypeIncludingAncestors(context, type, true, false);
    }
}
