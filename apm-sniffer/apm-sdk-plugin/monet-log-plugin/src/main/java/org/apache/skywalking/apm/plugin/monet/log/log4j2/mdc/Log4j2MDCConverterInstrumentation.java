package org.apache.skywalking.apm.plugin.monet.log.log4j2.mdc;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.ClassInstanceMethodsEnhancePluginDefineV2;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.v2.InstanceMethodsInterceptV2Point;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;
import org.apache.skywalking.apm.agent.core.plugin.match.NameMatch;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.apache.skywalking.apm.agent.core.plugin.bytebuddy.ArgumentTypeNameMatch.takesArgumentWithType;

/**
 * Log4j2MDCConverterInstrumentation
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class Log4j2MDCConverterInstrumentation extends ClassInstanceMethodsEnhancePluginDefineV2 {

    public static final String ENHANCE_CLASS = "com.fakebilly.monet.log.log4j2.mdc.Log4j2MdcPatternConverter";

    public static final String INTERCEPT_TRACE_ID_CLASS = "org.apache.skywalking.apm.plugin.monet.log.log4j2.mdc.MDCTraceIdInterceptor";
    public static final String ENHANCE_TRACE_ID_METHOD = "convertTraceId";
    public static final String ENHANCE_TRACE_ID_METHOD_WITH = "org.apache.logging.log4j.core.LogEvent";

    public static final String INTERCEPT_HOST_CLASS = "org.apache.skywalking.apm.plugin.monet.log.log4j2.mdc.MDCHostInterceptor";
    public static final String ENHANCE_HOST_METHOD = "convertHost";
    public static final String ENHANCE_HOST_METHOD_WITH = "org.apache.logging.log4j.core.LogEvent";

    public static final String INTERCEPT_APPLICATION_NAME_CLASS = "org.apache.skywalking.apm.plugin.monet.log.log4j2.mdc.MDCApplicationNameInterceptor";
    public static final String ENHANCE_APPLICATION_NAME_METHOD = "convertApplicationName";
    public static final String ENHANCE_APPLICATION_NAME_METHOD_WITH = "org.apache.logging.log4j.core.LogEvent";

    @Override
    public ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return null;
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
