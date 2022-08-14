package org.apache.skywalking.apm.plugin.monet.log.logback.mdc;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.ClassInstanceMethodsEnhancePluginDefineV2;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.v2.InstanceMethodsInterceptV2Point;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;
import org.apache.skywalking.apm.agent.core.plugin.match.NameMatch;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.apache.skywalking.apm.agent.core.plugin.bytebuddy.ArgumentTypeNameMatch.takesArgumentWithType;

/**
 * LogbackMDCConverterInstrumentation
 * Support MDC https://logback.qos.ch/manual/mdc.html
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class LogbackMDCConverterInstrumentation extends ClassInstanceMethodsEnhancePluginDefineV2 {

    public static final String ENHANCE_CLASS = "com.fakebilly.monet.log.logback.mdc.LogbackMDCPatternConverter";

    private static final String INIT_METHOD_INTERCEPTOR = "org.apache.skywalking.apm.plugin.monet.log.logback.mdc.LogbackMDCConverterInterceptor";

    public static final String INTERCEPT_TRACE_ID_CLASS = "org.apache.skywalking.apm.plugin.monet.log.logback.mdc.MDCTraceIdInterceptor";
    public static final String ENHANCE_TRACE_ID_METHOD = "convertTraceId";
    public static final String ENHANCE_TRACE_ID_METHOD_WITH = "ch.qos.logback.classic.spi.ILoggingEvent";

    public static final String INTERCEPT_HOST_CLASS = "org.apache.skywalking.apm.plugin.monet.log.logback.mdc.MDCHostInterceptor";
    public static final String ENHANCE_HOST_METHOD = "convertHost";
    public static final String ENHANCE_HOST_METHOD_WITH = "ch.qos.logback.classic.spi.ILoggingEvent";

    public static final String INTERCEPT_APPLICATION_NAME_CLASS = "org.apache.skywalking.apm.plugin.monet.log.logback.mdc.MDCApplicationNameInterceptor";
    public static final String ENHANCE_APPLICATION_NAME_METHOD = "convertApplicationName";
    public static final String ENHANCE_APPLICATION_NAME_METHOD_WITH = "ch.qos.logback.classic.spi.ILoggingEvent";

    @Override
    public ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[] {
                new ConstructorInterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getConstructorMatcher() {
                        return any();
                    }

                    @Override
                    public String getConstructorInterceptor() {
                        return INIT_METHOD_INTERCEPTOR;
                    }
                }
        };
    }

    @Override
    public InstanceMethodsInterceptV2Point[] getInstanceMethodsInterceptV2Points() {
        return new InstanceMethodsInterceptV2Point[]{
                new InstanceMethodsInterceptV2Point() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return named(ENHANCE_TRACE_ID_METHOD).and(takesArgumentWithType(0, ENHANCE_TRACE_ID_METHOD_WITH));
                    }

                    @Override
                    public String getMethodsInterceptorV2() {
                        return INTERCEPT_TRACE_ID_CLASS;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return false;
                    }
                },
                new InstanceMethodsInterceptV2Point() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return named(ENHANCE_HOST_METHOD).and(takesArgumentWithType(0, ENHANCE_HOST_METHOD_WITH));
                    }

                    @Override
                    public String getMethodsInterceptorV2() {
                        return INTERCEPT_HOST_CLASS;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return false;
                    }
                },
                new InstanceMethodsInterceptV2Point() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return named(ENHANCE_APPLICATION_NAME_METHOD).and(takesArgumentWithType(0, ENHANCE_APPLICATION_NAME_METHOD_WITH));
                    }

                    @Override
                    public String getMethodsInterceptorV2() {
                        return INTERCEPT_APPLICATION_NAME_CLASS;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return false;
                    }
                }
        };
    }

    @Override
    protected ClassMatch enhanceClass() {
        return NameMatch.byName(ENHANCE_CLASS);
    }
}
