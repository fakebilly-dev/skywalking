package org.apache.skywalking.apm.plugin.monet.log.logback.convert;

import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.InstanceMethodsAroundInterceptorV2;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.MethodInvocationContext;
import org.apache.skywalking.apm.plugin.monet.log.utils.TraceIdUtil;
import org.apache.skywalking.apm.toolkit.logging.common.log.SkyWalkingContext;

import java.lang.reflect.Method;

/**
 * TraceIdPatternConverterInterceptor
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class TraceIdPatternConverterInterceptor implements InstanceMethodsAroundInterceptorV2 {

    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, MethodInvocationContext context) throws Throwable {

    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object ret, MethodInvocationContext context) throws Throwable {
        if (!ContextManager.isActive()) {
            if (allArguments[0] instanceof EnhancedInstance) {
                SkyWalkingContext skyWalkingContext = (SkyWalkingContext) ((EnhancedInstance) allArguments[0]).getSkyWalkingDynamicField();
                if (skyWalkingContext != null) {
                    return TraceIdUtil.getTraceId(skyWalkingContext.getTraceId());
                }
            }
        }
        return TraceIdUtil.getTraceId(ContextManager.getGlobalTraceId());
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Throwable t, MethodInvocationContext context) {

    }
}
