package org.apache.skywalking.apm.plugin.monet.log.log4j2.async;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.ClassInstanceMethodsEnhancePluginDefineV2;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.v2.InstanceMethodsInterceptV2Point;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.apache.skywalking.apm.agent.core.plugin.match.NameMatch.byName;

/**
 * Log4jLogEventInstrumentation
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class Log4jLogEventInstrumentation extends ClassInstanceMethodsEnhancePluginDefineV2 {

    private static final String INTERCEPTOR = "org.apache.skywalking.apm.plugin.monet.log.log4j2.async.Log4jLogEventConstructorInterceptor";
    private static final String ENHANCE_CLASS = "org.apache.logging.log4j.core.impl.Log4jLogEvent";

    @Override
    public ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[]{
                new ConstructorInterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getConstructorMatcher() {
                        return any();
                    }

                    @Override
                    public String getConstructorInterceptor() {
                        return INTERCEPTOR;
                    }
                }
        };
    }

    @Override
    public InstanceMethodsInterceptV2Point[] getInstanceMethodsInterceptV2Points() {
        return new InstanceMethodsInterceptV2Point[0];
    }

    @Override
    protected ClassMatch enhanceClass() {
        return byName(ENHANCE_CLASS);
    }
}

