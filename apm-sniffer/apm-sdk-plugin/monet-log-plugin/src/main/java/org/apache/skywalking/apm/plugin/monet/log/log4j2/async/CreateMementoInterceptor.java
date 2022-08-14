package org.apache.skywalking.apm.plugin.monet.log.log4j2.async;

import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.MethodInvocationContext;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.StaticMethodsAroundInterceptorV2;

import java.lang.reflect.Method;

/**
 * CreateMementoInterceptor
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class CreateMementoInterceptor implements StaticMethodsAroundInterceptorV2 {

    @Override
    public void beforeMethod(Class clazz, Method method, Object[] allArguments, Class<?>[] parameterTypes, MethodInvocationContext context) {

    }

    @Override
    public Object afterMethod(Class clazz, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object ret, MethodInvocationContext context) {
        if (ret instanceof EnhancedInstance && allArguments[0] instanceof EnhancedInstance) {
            EnhancedInstance instance = (EnhancedInstance) ret;
            EnhancedInstance oldInstance = (EnhancedInstance) allArguments[0];
            instance.setSkyWalkingDynamicField(oldInstance.getSkyWalkingDynamicField());
        }
        return ret;
    }

    @Override
    public void handleMethodException(Class clazz, Method method, Object[] allArguments, Class<?>[] parameterTypes, Throwable t, MethodInvocationContext context) {

    }
}
