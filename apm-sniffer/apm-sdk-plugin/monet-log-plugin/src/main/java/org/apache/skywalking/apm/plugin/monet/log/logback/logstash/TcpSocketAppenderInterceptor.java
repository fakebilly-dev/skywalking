package org.apache.skywalking.apm.plugin.monet.log.logback.logstash;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.skywalking.apm.agent.core.conf.Config;
import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.logging.api.ILog;
import org.apache.skywalking.apm.agent.core.logging.api.LogManager;
import org.apache.skywalking.apm.agent.core.os.OSUtil;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.MethodInterceptResult;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * TcpSocketAppenderInterceptor
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class TcpSocketAppenderInterceptor implements InstanceMethodsAroundInterceptor {

    private static final ILog LOGGER = LogManager.getLogger(TcpSocketAppenderInterceptor.class);

    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
                             MethodInterceptResult result) throws Throwable {
        ILoggingEvent event = (ILoggingEvent) allArguments[0];
        if (event != null && event.getLoggerContextVO() != null && event.getLoggerContextVO()
                .getPropertyMap() != null) {
            Map<String, String> propertyMap = event.getLoggerContextVO().getPropertyMap();
            propertyMap.put("traceId", ContextManager.getGlobalTraceId());
            propertyMap.put("applicationName", Config.Agent.SERVICE_NAME);
            propertyMap.put("host", OSUtil.getIPV4());
        }
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
                              Object ret) throws Throwable {
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
                                      Class<?>[] argumentsTypes, Throwable t) {

    }
}
