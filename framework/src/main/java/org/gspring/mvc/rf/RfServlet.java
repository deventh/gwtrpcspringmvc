package org.gspring.mvc.rf;

import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

class RfServlet extends RequestFactoryServlet {
    private ServletContext servletContext;
    private ServletConfig servletConfig;

    public RfServlet() {
        super();
    }

    public RfServlet(ExceptionHandler exceptionHandler, ServiceLayerDecorator... serviceDecorators) {
        super(exceptionHandler, serviceDecorators);
    }

    public void render(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        super.doPost(request, response);
    }

    @Override
    public ServletConfig getServletConfig() {
        return new ServletConfigStub();
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Need to stub real config as because we
     * do not have access to specific servlet parameters
     * since we use DispatcherServlet and Spring MVC approach
     */
    private class ServletConfigStub implements ServletConfig {
        @Override
        public String getServletName() {
            return null;
        }

        @Override
        public ServletContext getServletContext() {
            return servletContext;
        }

        @Override
        public String getInitParameter(String s) {
            return null;
        }

        @Override
        public Enumeration getInitParameterNames() {
            return null;
        }
    }
}
