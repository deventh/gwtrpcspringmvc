package org.gspring.mvc.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * MVC Handler Mapping for GWT RPC calls
 */
@Component
public class RpcGwtHandlerMapping extends AbstractDetectingUrlHandlerMapping {
    protected final Log logger = LogFactory.getLog(getClass());

    public static final String DELEGATE_MODEL_KEY = "delegate";

    @Autowired(required = false)
    private CacheManager cacheManager = new NoOpCacheManager();

    @Override
    protected String[] determineUrlsForHandler(String beanName) {
        BeanFactory context = getApplicationContext();

        if (!context.isTypeMatch(beanName, RemoteService.class)) {
            return null;
        }

        RemoteService bean = context.getBean(beanName, RemoteService.class);

        Set<String> relativePaths = findRelativePaths(bean);

        return relativePaths.toArray(new String[0]);

    }

    private Set<String> findRelativePaths(RemoteService bean) {
        if (AopUtils.isCglibProxy(bean)) {
            return doFindRelativePaths(AopUtils.getTargetClass(bean));
        } else {
            return doFindRelativePaths(bean.getClass());
        }
    }

    private Set<String> doFindRelativePaths(Class<?> beanClass) {
        Set<String> paths = new HashSet<String>();

        for (Class<?> interf : beanClass.getInterfaces()) {
            if (RemoteService.class.equals(interf)) {
                RemoteServiceRelativePath relativePathAnnotation = beanClass.getAnnotation(RemoteServiceRelativePath.class);
                if (relativePathAnnotation != null) {
                    paths.add(relativePathAnnotation.value());
                } else {
                    logger.warn("Bean of type [" + beanClass + "] does not have annotation of type [" + RemoteServiceRelativePath.class + "] and is skipped");
                }
            } else {
                paths.addAll(doFindRelativePaths(interf));
            }
        }

        return paths;
    }

    @Override
    public Object lookupHandler(String urlPath, HttpServletRequest request) throws Exception {
        ValueWrapper cached = getCache().get(urlPath);
        if (cached != null) {
            return cached.get();
        }

        for (String relativePath : getHandlerMap().keySet()) {
            if (urlPath.endsWith(relativePath)) {
                Object value = getHandlerMap().get(relativePath);

                getCache().put(urlPath, value);

                return value;
            }
        }

        return null;
    }

    private Cache getCache() {
        final String name = getClass().getCanonicalName();
        return cacheManager.getCache(name);
    }
}
