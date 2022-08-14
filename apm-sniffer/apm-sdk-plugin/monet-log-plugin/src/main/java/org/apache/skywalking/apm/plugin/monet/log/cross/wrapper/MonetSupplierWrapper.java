package org.apache.skywalking.apm.plugin.monet.log.cross.wrapper;

import org.apache.skywalking.apm.agent.core.context.ContextSnapshot;

import java.util.function.Supplier;

/**
 * MonetSupplierWrapper
 * @author FakeBilly
 * @date 2021-12-24 10:24
 * @version V1.0
 * @github https://github.com/fakebilly-dev/monet
 **/
public class MonetSupplierWrapper<V> implements Supplier<V> {

    final Supplier<V> supplier;

    private ContextSnapshot contextSnapshot;

    public static <V> MonetSupplierWrapper<V> of(Supplier<V> r) {
        return new MonetSupplierWrapper<>(r);
    }

    public static <V> MonetSupplierWrapper<V> of(Supplier<V> r, ContextSnapshot contextSnapshot) {
        return new MonetSupplierWrapper<>(r, contextSnapshot);
    }

    public MonetSupplierWrapper(Supplier<V> supplier) {
        this.supplier = supplier;
    }

    public MonetSupplierWrapper(Supplier<V> supplier, ContextSnapshot contextSnapshot) {
        this.supplier = supplier;
        this.contextSnapshot = contextSnapshot;
    }

    @Override
    public V get() {
        return supplier.get();
    }
}
