package org.apache.skywalking.apm.plugin.monet.log.logback.async;

import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.logging.api.ILog;
import org.apache.skywalking.apm.agent.core.logging.api.LogManager;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.InstanceMethodsAroundInterceptorV2;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.MethodInvocationContext;
import org.apache.skywalking.apm.toolkit.logging.common.log.SkyWalkingContext;

import java.lang.reflect.Method;

/**
 * AsyncAppenderBaseMethodInterceptor
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class AsyncAppenderBaseMethodInterceptor implements InstanceMethodsAroundInterceptorV2 {

    private static final ILog LOGGER = LogManager.getLogger(AsyncAppenderBaseMethodInterceptor.class);

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
