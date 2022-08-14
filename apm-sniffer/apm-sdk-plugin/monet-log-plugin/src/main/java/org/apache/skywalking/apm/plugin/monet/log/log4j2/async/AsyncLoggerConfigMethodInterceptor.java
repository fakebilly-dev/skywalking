package org.apache.skywalking.apm.plugin.monet.log.log4j2.async;

import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.InstanceMethodsAroundInterceptorV2;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.MethodInvocationContext;
import org.apache.skywalking.apm.toolkit.logging.common.log.SkyWalkingContext;

import java.lang.reflect.Method;

/**
 * AsyncLoggerConfigMethodInterceptor
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class AsyncLoggerConfigMethodInterceptor implements InstanceMethodsAroundInterceptorV2 {

    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, MethodInvocationContext context) throws Throwable {
        if (allArguments[0] instanceof EnhancedInstance) {
            SkyWalkingContext skyWalkingContext = new SkyWalkingContext(ContextManager.getGlobalTraceId(),
                    ContextManager.getSegmentId(), ContextManager.getSpanId());

            EnhancedInstance instances = (EnhancedInstance) allArguments[0];
            instances.setSkyWalkingDynamicField(skyWalkingContext);
        }
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object ret, MethodInvocationContext context) throws Throwable {
        return null;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Throwable t, MethodInvocationContext context) {

    }
}
