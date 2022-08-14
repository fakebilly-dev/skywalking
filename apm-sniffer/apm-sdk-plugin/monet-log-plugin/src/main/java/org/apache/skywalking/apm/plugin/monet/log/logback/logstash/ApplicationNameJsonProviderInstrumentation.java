package org.apache.skywalking.apm.plugin.monet.log.logback.logstash;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.ClassInstanceMethodsEnhancePluginDefineV2;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.v2.InstanceMethodsInterceptV2Point;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;
import org.apache.skywalking.apm.agent.core.plugin.match.NameMatch;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * ApplicationNameJsonProviderInstrumentation
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class ApplicationNameJsonProviderInstrumentation extends ClassInstanceMethodsEnhancePluginDefineV2 {

    private static final String INTERCEPT_CLASS = "org.apache.skywalking.apm.plugin.monet.log.logback.logstash.ApplicationNameJsonProviderInterceptor";
    private static final String ENHANCE_CLASS = "com.fakebilly.monet.log.logback.logstash.ApplicationNameJsonProvider";
    private static final String ENHANCE_METHOD = "getApplicationName";

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
        return NameMatch.byName(ENHANCE_CLASS);
    }
}
