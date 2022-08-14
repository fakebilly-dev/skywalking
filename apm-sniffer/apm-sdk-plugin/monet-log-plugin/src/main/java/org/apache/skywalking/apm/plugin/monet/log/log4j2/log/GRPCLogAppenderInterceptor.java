package org.apache.skywalking.apm.plugin.monet.log.log4j2.log;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.skywalking.apm.agent.core.boot.ServiceManager;
import org.apache.skywalking.apm.agent.core.conf.Config;
import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.context.util.ThrowableTransformer;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.InstanceMethodsAroundInterceptorV2;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.MethodInvocationContext;
import org.apache.skywalking.apm.agent.core.remote.LogReportServiceClient;
import org.apache.skywalking.apm.network.common.v3.KeyStringValuePair;
import org.apache.skywalking.apm.network.logging.v3.*;
import org.apache.skywalking.apm.toolkit.logging.common.log.ToolkitConfig;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * GRPCLogAppenderInterceptor
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class GRPCLogAppenderInterceptor implements InstanceMethodsAroundInterceptorV2 {

    private LogReportServiceClient client;

    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, MethodInvocationContext context) throws Throwable {
        if (Objects.isNull(client)) {
            client = ServiceManager.INSTANCE.findService(LogReportServiceClient.class);
            if (Objects.isNull(client)) {
                return;
            }
        }
        LogEvent event = (LogEvent) allArguments[0];
        if (Objects.nonNull(event)) {
            client.produce(transform((AbstractAppender) objInst, event));
        }
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object ret, MethodInvocationContext context) throws Throwable {
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Throwable t, MethodInvocationContext context) {

    }

    private LogData transform(final AbstractAppender appender, LogEvent event) {
        LogTags.Builder logTags = LogTags.newBuilder()
                .addData(KeyStringValuePair.newBuilder()
                        .setKey("level").setValue(event.getLevel().toString()).build())
                .addData(KeyStringValuePair.newBuilder()
                        .setKey("logger").setValue(event.getLoggerName()).build())
                .addData(KeyStringValuePair.newBuilder()
                        .setKey("thread").setValue(event.getThreadName()).build());
        if (!ToolkitConfig.Plugin.Toolkit.Log.TRANSMIT_FORMATTED) {
            if (event.getMessage().getParameters() != null) {
                for (int i = 0; i < event.getMessage().getParameters().length; i++) {
                    String value = Optional.ofNullable(event.getMessage().getParameters()[i]).orElse("null").toString();
                    logTags.addData(KeyStringValuePair.newBuilder()
                            .setKey("argument." + i).setValue(value).build());
                }
            }

            if (event.getThrown() != null) {
                logTags.addData(KeyStringValuePair.newBuilder()
                        .setKey("exception").setValue(ThrowableTransformer.INSTANCE.convert2String(event.getThrown(), 2048)).build());
            }
        }

        LogData.Builder builder = LogData.newBuilder()
                .setTimestamp(event.getTimeMillis())
                .setService(Config.Agent.SERVICE_NAME)
                .setServiceInstance(Config.Agent.INSTANCE_NAME)
                .setTags(logTags.build())
                .setBody(LogDataBody.newBuilder().setType(LogDataBody.ContentCase.TEXT.name())
                        .setText(TextLog.newBuilder().setText(transformLogText(appender, event)).build()).build());
        return -1 == ContextManager.getSpanId() ? builder.build()
                : builder.setTraceContext(TraceContext.newBuilder()
                .setTraceId(ContextManager.getGlobalTraceId())
                .setSpanId(ContextManager.getSpanId())
                .setTraceSegmentId(ContextManager.getSegmentId())
                .build()).build();
    }

    private String transformLogText(final AbstractAppender appender, final LogEvent event) {
        if (ToolkitConfig.Plugin.Toolkit.Log.TRANSMIT_FORMATTED) {
            if (appender.getLayout() != null) {
                return new String(appender.getLayout().toByteArray(event));
            }
            return event.getMessage().getFormattedMessage() + "\n" + ThrowableTransformer.INSTANCE.convert2String(event.getThrown(), 2048);
        } else {
            return event.getMessage().getFormat();
        }
    }
}
