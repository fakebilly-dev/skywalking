package org.apache.skywalking.apm.plugin.monet.log.log4j2.log;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.ClassInstanceMethodsEnhancePluginDefineV2;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.v2.InstanceMethodsInterceptV2Point;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.apache.skywalking.apm.agent.core.plugin.match.NameMatch.byName;

/**
 * GRPCLogAppenderInstrumentation
 * enhance the method append of the grpc log send lo4j2 class "org.apache.skywalking.apm.toolkit.log.log4j.v2.x.log
 * .GRPCLogClientAppender".
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class GRPCLogAppenderInstrumentation extends ClassInstanceMethodsEnhancePluginDefineV2 {

    public static final String INTERCEPT_CLASS = "org.apache.skywalking.apm.plugin.monet.log.log4j2.log.GRPCLogAppenderInterceptor";
    public static final String ENHANCE_CLASS = "com.fakebilly.monet.log.log4j2.log.GRPCLogClientAppender";
    public static final String ENHANCE_METHOD = "append";

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
                        return named(ENHANCE_METHOD);
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
