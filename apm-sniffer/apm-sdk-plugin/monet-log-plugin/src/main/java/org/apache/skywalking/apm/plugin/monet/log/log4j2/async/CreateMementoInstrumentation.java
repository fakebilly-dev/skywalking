package org.apache.skywalking.apm.plugin.monet.log.log4j2.async;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.v2.ClassEnhancePluginDefineV2;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.v2.InstanceMethodsInterceptV2Point;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.v2.StaticMethodsInterceptV2Point;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;
import static org.apache.skywalking.apm.agent.core.plugin.match.NameMatch.byName;

/**
 * CreateMementoInstrumentation
 * @author FakeBilly
 * @version V1.0.0
 * @github https://github.com/fakebilly-dev/skywalking
 **/
public class CreateMementoInstrumentation extends ClassEnhancePluginDefineV2 {

    private static final String INTERCEPTOR = "org.apache.skywalking.apm.plugin.monet.log.log4j2.async.CreateMementoInterceptor";
    private static final String ENHANCE_SUPER_CLASS = "org.apache.logging.log4j.core.impl.Log4jLogEvent";
    private static final String ENHANCE_METHOD = "createMemento";

    @Override
    public ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }


    @Override
    public InstanceMethodsInterceptV2Point[] getInstanceMethodsInterceptV2Points() {
        return new InstanceMethodsInterceptV2Point[0];
    }


    @Override
    public StaticMethodsInterceptV2Point[] getStaticMethodsInterceptV2Points() {
        return new StaticMethodsInterceptV2Point[]{
                new StaticMethodsInterceptV2Point() {

                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return named(ENHANCE_METHOD).and(takesArguments(2));
                    }

                    @Override
                    public String getMethodsInterceptorV2() {
                        return INTERCEPTOR;
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
        return byName(ENHANCE_SUPER_CLASS);
    }

    @Override
    public boolean isBootstrapInstrumentation() {
        return false;
    }
}