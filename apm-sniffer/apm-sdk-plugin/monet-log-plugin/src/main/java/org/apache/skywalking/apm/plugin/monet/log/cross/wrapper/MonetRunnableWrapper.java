package org.apache.skywalking.apm.plugin.monet.log.cross.wrapper;

import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.context.ContextSnapshot;
import org.apache.skywalking.apm.agent.core.context.trace.AbstractSpan;
import org.apache.skywalking.apm.network.trace.component.ComponentsDefine;

/**
 * MonetRunnableWrapper
 * @author FakeBilly
 * @date 2021-12-24 10:24
 * @version V1.0
 * @github https://github.com/fakebilly-dev/monet
 **/
public class MonetRunnableWrapper implements Runnable {

    final Runnable runnable;

    private ContextSnapshot contextSnapshot;

    public MonetRunnableWrapper(Runnable runnable) {
        this.runnable = runnable;
    }

    public MonetRunnableWrapper(Runnable runnable, ContextSnapshot contextSnapshot) {
        this.runnable = runnable;
        this.contextSnapshot = contextSnapshot;
    }

    public static MonetRunnableWrapper of(Runnable r) {
        return new MonetRunnableWrapper(r);
    }

    public static MonetRunnableWrapper of(Runnable r, ContextSnapshot contextSnapshot) {
        return new MonetRunnableWrapper(r, contextSnapshot);
    }

    @Override
    public void run() {
        AbstractSpan span = ContextManager.createLocalSpan("MonetRunnableWrapper");
        span.setComponent(ComponentsDefine.JDK_THREADING);
        ContextManager.continued(contextSnapshot);
        this.runnable.run();
        ContextManager.stopSpan();
    }
}