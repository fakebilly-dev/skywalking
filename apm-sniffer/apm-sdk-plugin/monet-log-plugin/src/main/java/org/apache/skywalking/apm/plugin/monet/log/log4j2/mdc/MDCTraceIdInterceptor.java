package org.apache.skywalking.apm.plugin.monet.log.log4j2.mdc;

import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.InstanceMethodsAroundInterceptorV2;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.MethodInvocationContext;
import org.apache.skywalking.apm.plugin.monet.log.utils.TraceIdUtil;
import org.apache.skywalking.apm.toolkit.logging.common.log.SkyWalkingContext;

import java.lang.reflect.Method;

/**
 * MDCTraceIdInterceptor
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class MDCTraceIdInterceptor implements InstanceMethodsAroundInterceptorV2 {

    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, MethodInvocationContext context) throws Throwable {
        String traceId = "";
        // Async Thread, where ContextManager is not active
        if (!ContextManager.isActive() && allArguments[0] instanceof EnhancedInstance) {
            SkyWalkingContext skyWalkingContext = (SkyWalkingContext) ((EnhancedInstance) allArguments[0]).getSkyWalkingDynamicField();
            traceId = null == skyWalkingContext ? "" : skyWalkingContext.getTraceId();
        } else {
            traceId = ContextManager.getGlobalTraceId();
        }
        ((StringBuilder) allArguments[1]).append(TraceIdUtil.getTraceId(traceId));
        context.defineReturnValue(null);
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object ret, MethodInvocationContext context) throws Throwable {
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Throwable t, MethodInvocationContext context) {

    }
}
