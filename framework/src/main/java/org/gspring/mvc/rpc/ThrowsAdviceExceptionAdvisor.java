package org.gspring.mvc.rpc;

import org.aopalliance.aop.Advice;
import org.springframework.aop.AfterAdvice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.framework.adapter.ThrowsAdviceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThrowsAdviceExceptionAdvisor implements PointcutAdvisor {
  @Autowired(required = false)
  private ExceptionsTransformer exceptionsTransformer;

  @Override
  public Pointcut getPointcut() {
    return Pointcut.TRUE;
  }

  @Override
  public Advice getAdvice() {
    return new ThrowsAdviceInterceptor(new AfterAdviceImpl(exceptionsTransformer));
  }

  @Override
  public boolean isPerInstance() {
    return false;
  }

  public static class AfterAdviceImpl implements AfterAdvice {
    private ExceptionsTransformer exceptionsTransformer;

    public AfterAdviceImpl(ExceptionsTransformer exceptionsTransformer) {
      this.exceptionsTransformer = exceptionsTransformer;
    }

    public void afterThrowing(Exception ex) {
      if(exceptionsTransformer != null) {
        RuntimeException transformed = exceptionsTransformer.transform(ex);

        if(transformed != null) {
          throw transformed;
        }
      }
    }
  }
}
