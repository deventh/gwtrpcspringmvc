package org.gspring.mvc.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.View;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
class RpcGwtView implements View, ServletContextAware {
    private ServletContext servletContext;

    @Resource
    private ThrowsAdviceExceptionAdvisor throwsAdviceExceptionAdvisor;

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object delegate = model.get(RpcGwtHandlerMapping.DELEGATE_MODEL_KEY);

        ProxyFactory pf = new ProxyFactory();
        pf.setProxyTargetClass(true);
        pf.setTarget(delegate);
        pf.addAdvisor(0, throwsAdviceExceptionAdvisor);
        pf.setFrozen(true);

        RemoteServiceServlet rpcServlet = new RemoteServiceServlet(pf.getProxy()) {
            private static final long serialVersionUID = -7744815355232246902L;

            @Override
            public ServletContext getServletContext() {
                return servletContext;
            }

            @Override
            public String getServletName() {
                return RemoteServiceServlet.class.getName();
            }
        };

        rpcServlet.doPost(request, response);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
