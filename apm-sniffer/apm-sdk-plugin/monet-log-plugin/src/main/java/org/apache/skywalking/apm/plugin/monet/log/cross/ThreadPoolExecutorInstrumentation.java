package org.apache.skywalking.apm.plugin.monet.log.cross;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.ClassEnhancePluginDefineV2;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.v2.InstanceMethodsInterceptV2Point;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.v2.StaticMethodsInterceptV2Point;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;
import org.apache.skywalking.apm.agent.core.plugin.match.NameMatch;

/**
 * ThreadPoolExecutorInstrumentation
 * @author FakeBilly
 * @date 2021-12-24 10:24
 * @version V1.0
 * @github https://github.com/fakebilly-dev/monet
 **/
public class ThreadPoolExecutorInstrumentation extends ClassEnhancePluginDefineV2 {

    private static final String INTERCEPT_EXECUTE_CLASS = "org.apache.skywalking.apm.plugin.monet.log.cross.ThreadPoolExecutorMethodInterceptor";

    private static final String ENHANCE_CLASS = "java.util.concurrent.ThreadPoolExecutor";
    private static final String ENHANCE_EXECUTE_METHOD = "execute";
    private static final String ENHANCE_SUBMIT_METHOD = "submit";

    @Override
    protected ClassMatch enhanceClass() {
        return NameMatch.byName(ENHANCE_CLASS);
    }

    @Override
    public ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override
    public InstanceMethodsInterceptV2Point[] getInstanceMethodsInterceptV2Points() {
        return new InstanceMethodsInterceptV2Point[]{
                new InstanceMethodsInterceptV2Point() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return ElementMatchers.namedOneOf(ENHANCE_EXECUTE_METHOD, ENHANCE_SUBMIT_METHOD);
                    }

                    @Override
                    public String getMethodsInterceptorV2() {
                        return INTERCEPT_EXECUTE_CLASS;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return true;
                    }
                }
        };
    }

    @Override
    public StaticMethodsInterceptV2Point[] getStaticMethodsInterceptV2Points() {
        return new StaticMethodsInterceptV2Point[0];
    }

    @Override
    public boolean isBootstrapInstrumentation() {
        return true;
    }

}