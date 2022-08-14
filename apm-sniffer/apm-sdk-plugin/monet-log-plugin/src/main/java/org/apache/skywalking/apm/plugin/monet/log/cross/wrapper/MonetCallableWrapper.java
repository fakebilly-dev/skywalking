package org.apache.skywalking.apm.plugin.monet.log.cross.wrapper;

import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.context.ContextSnapshot;
import org.apache.skywalking.apm.agent.core.context.trace.AbstractSpan;
import org.apache.skywalking.apm.network.trace.component.ComponentsDefine;

import java.util.concurrent.Callable;

/**
 * MonetCallableWrapper
 * @author FakeBilly
 * @date 2021-12-24 10:24
 * @version V1.0
 * @github https://github.com/fakebilly-dev/monet
 **/
public class MonetCallableWrapper<V> implements Callable<V> {

    final Callable<V> callable;

    private ContextSnapshot contextSnapshot;

    public static <V> MonetCallableWrapper<V> of(Callable<V> r) {
        return new MonetCallableWrapper<>(r);
    }

    public static <V> MonetCallableWrapper<V> of(Callable<V> r, ContextSnapshot contextSnapshot) {
        return new MonetCallableWrapper<>(r, contextSnapshot);
    }

    public MonetCallableWrapper(Callable<V> callable) {
        this.callable = callable;
    }

    public MonetCallableWrapper(Callable<V> callable, ContextSnapshot contextSnapshot) {
        this.callable = callable;
        this.contextSnapshot = contextSnapshot;
    }

    @Override
    public V call() throws Exception {
        AbstractSpan span = ContextManager.createLocalSpan("MonetCallableWrapper");
        span.setComponent(ComponentsDefine.JDK_THREADING);
        ContextManager.continued(contextSnapshot);
        V call = callable.call();
        ContextManager.stopSpan();
        return call;
    }
}
