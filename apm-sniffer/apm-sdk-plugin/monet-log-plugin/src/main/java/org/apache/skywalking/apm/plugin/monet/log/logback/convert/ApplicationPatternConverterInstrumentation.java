package org.apache.skywalking.apm.plugin.monet.log.logback.convert;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.ClassInstanceMethodsEnhancePluginDefineV2;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.v2.InstanceMethodsInterceptV2Point;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.apache.skywalking.apm.agent.core.plugin.bytebuddy.ArgumentTypeNameMatch.takesArgumentWithType;
import static org.apache.skywalking.apm.agent.core.plugin.match.NameMatch.byName;

/**
 * ApplicationPatternConverterInstrumentation
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class ApplicationPatternConverterInstrumentation extends ClassInstanceMethodsEnhancePluginDefineV2 {

    public static final String INTERCEPT_CLASS = "org.apache.skywalking.apm.plugin.monet.log.logback.convert.ApplicationNamePatternConverterInterceptor";
    public static final String ENHANCE_CLASS = "com.fakebilly.monet.log.logback.convert.ApplicationNamePatternConverter";
    public static final String ENHANCE_METHOD = "convert";
    public static final String ENHANCE_METHOD_WITH = "ch.qos.logback.classic.spi.ILoggingEvent";

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
                        return named(ENHANCE_METHOD).and(takesArgumentWithType(0, ENHANCE_METHOD_WITH));
                    }

                    @Override
                    public String getMethodsInterceptorV2() {
                        return INTERCEPT_CLASS;
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
        return byName(ENHANCE_CLASS);
    }
}
