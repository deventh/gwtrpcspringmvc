package org.gspring.mvc.rf;

import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

@Component("rfGwtHandlerMapping")
class RfGwtHandlerMapping implements HandlerMapping {
    private static final String DEFAULT_URL = "/" + DefaultRequestTransport.URL;

    private String url;

    public RfGwtHandlerMapping() {
        this(DEFAULT_URL);
    }

    public RfGwtHandlerMapping(String url) {
        this.url = url;

        if(!this.url.startsWith("/")) {
            this.url = "/" + this.url;
        }
    }

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        final String requestURI = request.getRequestURI();

        if (!requestURI.endsWith(url)) {
            return null;
        }

        return new HandlerExecutionChain(new RfHandlerMarker(){});
    }

    static interface RfHandlerMarker {
    }
}
