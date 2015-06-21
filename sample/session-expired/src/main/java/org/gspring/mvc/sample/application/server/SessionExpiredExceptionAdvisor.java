package org.gspring.mvc.sample.application.server;

import org.gspring.mvc.sample.application.shared.security.SessionExpiredExceptionApp;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class SessionExpiredExceptionAdvisor extends DefaultPointcutAdvisor {
  public SessionExpiredExceptionAdvisor() {
    super(new MethodBeforeAdvice() {

      @Override
      public void before(Method method, Object[] args, Object target) throws Throwable {
        throw new SessionExpiredExceptionApp();
      }
    });
  }
}
