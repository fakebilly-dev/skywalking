package org.apache.skywalking.apm.plugin.monet.log.log4j2.async;

import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceConstructorInterceptor;
import org.apache.skywalking.apm.toolkit.logging.common.log.SkyWalkingContext;

/**
 * Log4jLogEventConstructorInterceptor
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class Log4jLogEventConstructorInterceptor implements InstanceConstructorInterceptor {

    @Override
    public void onConstruct(EnhancedInstance objInst, Object[] allArguments) {
        SkyWalkingContext skyWalkingContext = new SkyWalkingContext(ContextManager.getGlobalTraceId(),
                ContextManager.getSegmentId(), ContextManager.getSpanId());
        objInst.setSkyWalkingDynamicField(skyWalkingContext);
    }
}
