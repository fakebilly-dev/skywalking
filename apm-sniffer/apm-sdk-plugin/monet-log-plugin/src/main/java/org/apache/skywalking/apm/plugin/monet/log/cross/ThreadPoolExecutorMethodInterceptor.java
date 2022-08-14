package org.apache.skywalking.apm.plugin.monet.log.cross;

import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.InstanceMethodsAroundInterceptorV2;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.MethodInvocationContext;
import org.apache.skywalking.apm.plugin.monet.log.cross.wrapper.MonetCallableWrapper;
import org.apache.skywalking.apm.plugin.monet.log.cross.wrapper.MonetRunnableWrapper;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * ThreadPoolExecutorMethodInterceptor
 * @author FakeBilly
 * @date 2021-12-24 10:24
 * @version V1.0
 * @github https://github.com/fakebilly-dev/monet
 **/
public class ThreadPoolExecutorMethodInterceptor implements InstanceMethodsAroundInterceptorV2 {

    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, MethodInvocationContext context) throws Throwable {
        if (ContextManager.isActive()) {
            Object argument = enhanceArgument(allArguments[0]);
            if (null != argument) {
                allArguments[0] = argument;
            }
        }
    }

    private Object enhanceArgument(Object argument) {
        if (null == argument) {
            return null;
        }

        if (argument instanceof Runnable) {
            return MonetRunnableWrapper.of((Runnable) argument, ContextManager.capture());
        }
        if (argument instanceof Callable) {
            return MonetCallableWrapper.of((Callable) argument, ContextManager.capture());
        }

        return null;
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object ret, MethodInvocationContext context) throws Throwable {
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Throwable t, MethodInvocationContext context) {

    }
}