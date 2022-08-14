package org.apache.skywalking.apm.plugin.monet.log.logback;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import com.fakebilly.monet.log.logback.layout.LogbackLayout;
import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.context.trace.AbstractSpan;
import org.apache.skywalking.apm.agent.core.logging.api.ILog;
import org.apache.skywalking.apm.agent.core.logging.api.LogManager;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.MethodInvocationContext;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.StaticMethodsAroundInterceptorV2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * LogbackInterceptor
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class LogbackInterceptor implements StaticMethodsAroundInterceptorV2 {

    private static final ILog LOGGER = LogManager.getLogger(LogbackInterceptor.class);

    public static final String FIELD_ENCODER = "encoder";

    @Override
    public void beforeMethod(Class clazz, Method method, Object[] allArguments, Class<?>[] parameterTypes, MethodInvocationContext context) {

    }

    @Override
    public Object afterMethod(Class clazz, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object ret, MethodInvocationContext context) {
        PatternLayoutEncoder patternLayoutEncoder = (PatternLayoutEncoder) allArguments[0];
        OutputStreamAppender<ILoggingEvent> outputStreamAppender = (OutputStreamAppender<ILoggingEvent>) allArguments[1];
        LayoutWrappingEncoder<ILoggingEvent> targetEncoder = new LayoutWrappingEncoder<>();
        targetEncoder.setContext(patternLayoutEncoder.getContext());
        targetEncoder.setCharset(StandardCharsets.UTF_8);
        LogbackLayout targetLayout = new LogbackLayout();
        targetLayout.setContext(patternLayoutEncoder.getContext());
        targetLayout.setPattern(patternLayoutEncoder.getPattern());
        targetEncoder.setLayout(targetLayout);
        targetEncoder.setImmediateFlush(true);
        try {
            Field encoderField = OutputStreamAppender.class.getDeclaredField(FIELD_ENCODER);
            encoderField.setAccessible(true);
            encoderField.set(outputStreamAppender, targetEncoder);
            targetLayout.start();
            targetEncoder.start();
            outputStreamAppender.start();
        } catch (Throwable t) {
            dealException(t);
        }
        return ret;
    }

    @Override
    public void handleMethodException(Class clazz, Method method, Object[] allArguments, Class<?>[] parameterTypes, Throwable t, MethodInvocationContext context) {
        dealException(t);
    }

    private void dealException(Throwable throwable) {
        AbstractSpan span = ContextManager.activeSpan();
        span.log(throwable);
    }
}
