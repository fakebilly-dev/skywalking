package org.apache.skywalking.apm.plugin.monet.log.logback;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.ClassStaticMethodsEnhancePluginDefineV2;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.v2.StaticMethodsInterceptV2Point;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;
import org.apache.skywalking.apm.agent.core.plugin.match.NameMatch;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * LogbackInstrumentation
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class LogbackInstrumentation extends ClassStaticMethodsEnhancePluginDefineV2 {

    private static final String INTERCEPT_CLASS = "org.apache.skywalking.apm.plugin.monet.log.logback.LogbackInterceptor";
    private static final String ENHANCE_CLASS = "com.fakebilly.monet.log.logback.config.LogbackConfig";
    private static final String ENHANCE_METHOD = "modifyPatternLayout";

    @Override
    public StaticMethodsInterceptV2Point[] getStaticMethodsInterceptV2Points() {
        return new StaticMethodsInterceptV2Point[]{
                new StaticMethodsInterceptV2Point() {

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
