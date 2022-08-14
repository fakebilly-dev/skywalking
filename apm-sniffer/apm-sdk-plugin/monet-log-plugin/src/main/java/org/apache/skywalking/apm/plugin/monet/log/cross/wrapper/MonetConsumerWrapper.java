package org.apache.skywalking.apm.plugin.monet.log.cross.wrapper;

import org.apache.skywalking.apm.agent.core.context.ContextSnapshot;

import java.util.function.Consumer;

/**
 * MonetConsumerWrapper
 * @author FakeBilly
 * @date 2021-12-24 10:24
 * @version V1.0
 * @github https://github.com/fakebilly-dev/monet
 **/
public class MonetConsumerWrapper<V> implements Consumer<V> {

    final Consumer<V> consumer;

    private ContextSnapshot contextSnapshot;

    public MonetConsumerWrapper(Consumer<V> consumer) {
        this.consumer = consumer;
    }

    public MonetConsumerWrapper(Consumer<V> consumer, ContextSnapshot contextSnapshot) {
        this.consumer = consumer;
        this.contextSnapshot = contextSnapshot;
    }

    public static <V> MonetConsumerWrapper<V> of(Consumer<V> consumer) {
        return new MonetConsumerWrapper<>(consumer);
    }

    public static <V> MonetConsumerWrapper<V> of(Consumer<V> consumer, ContextSnapshot contextSnapshot) {
        return new MonetConsumerWrapper<>(consumer, contextSnapshot);
    }

    @Override
    public void accept(V v) {
        this.consumer.accept(v);
    }

}
