package org.apache.skywalking.apm.plugin.monet.log.logback.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.OutputStreamAppender;
import org.apache.skywalking.apm.agent.core.boot.ServiceManager;
import org.apache.skywalking.apm.agent.core.conf.Config;
import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.context.util.ThrowableTransformer;
import org.apache.skywalking.apm.agent.core.logging.api.ILog;
import org.apache.skywalking.apm.agent.core.logging.api.LogManager;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.MethodInterceptResult;
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
public class GRPCLogAppenderInterceptor implements InstanceMethodsAroundInterceptor {

    private static final ILog LOGGER = LogManager.getLogger(GRPCLogAppenderInterceptor.class);

    private LogReportServiceClient client;

    @SuppressWarnings("unchecked")
    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
                             MethodInterceptResult result) throws Throwable {
        if (Objects.isNull(client)) {
            client = ServiceManager.INSTANCE.findService(LogReportServiceClient.class);
            if (Objects.isNull(client)) {
                return;
            }
        }
        ILoggingEvent event = (ILoggingEvent) allArguments[0];
        if (Objects.nonNull(event)) {
            client.produce(transform((OutputStreamAppender<ILoggingEvent>) objInst, event));
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

    /**
     * transforms {@link ILoggingEvent}  to {@link LogData}
     *
     * @param appender the real {@link OutputStreamAppender appender}
     * @param event {@link ILoggingEvent}
     * @return {@link LogData} with filtered trace context in order to reduce the cost on the network
     */
    private LogData transform(final OutputStreamAppender<ILoggingEvent> appender, ILoggingEvent event) {
        LogTags.Builder logTags = LogTags.newBuilder()
                .addData(KeyStringValuePair.newBuilder()
                        .setKey("level").setValue(event.getLevel().toString()).build())
                .addData(KeyStringValuePair.newBuilder()
                        .setKey("logger").setValue(event.getLoggerName()).build())
                .addData(KeyStringValuePair.newBuilder()
                        .setKey("thread").setValue(event.getThreadName()).build());
        if (!ToolkitConfig.Plugin.Toolkit.Log.TRANSMIT_FORMATTED) {
            if (event.getArgumentArray() != null) {
                for (int i = 0; i < event.getArgumentArray().length; i++) {
                    String value = Optional.ofNullable(event.getArgumentArray()[i]).orElse("null").toString();
                    logTags.addData(KeyStringValuePair.newBuilder()
                            .setKey("argument." + i).setValue(value).build());
                }
            }

            final IThrowableProxy throwableProxy = event.getThrowableProxy();
            if (throwableProxy instanceof ThrowableProxy) {
                Throwable throwable = ((ThrowableProxy) throwableProxy).getThrowable();
                logTags.addData(KeyStringValuePair.newBuilder()
                        .setKey("exception").setValue(ThrowableTransformer.INSTANCE.convert2String(throwable, 2048)).build());
            }
        }

        LogData.Builder builder = LogData.newBuilder()
                .setTimestamp(event.getTimeStamp())
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

    private String transformLogText(final OutputStreamAppender<ILoggingEvent> appender, final ILoggingEvent event) {
        if (ToolkitConfig.Plugin.Toolkit.Log.TRANSMIT_FORMATTED) {
            return new String(appender.getEncoder().encode(event));
        } else {
            return event.getMessage();
        }
    }
}
