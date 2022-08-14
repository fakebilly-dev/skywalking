package org.apache.skywalking.apm.plugin.monet.log.logback.async;

import org.apache.skywalking.apm.agent.core.logging.api.ILog;
import org.apache.skywalking.apm.agent.core.logging.api.LogManager;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceConstructorInterceptor;

/**
 * LoggingEventConstructorInterceptor
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class LoggingEventConstructorInterceptor implements InstanceConstructorInterceptor {

    private static final ILog LOGGER = LogManager.getLogger(LoggingEventConstructorInterceptor.class);

    @Override
    public void onConstruct(EnhancedInstance objInst, Object[] allArguments) {
    }
}
