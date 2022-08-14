package org.apache.skywalking.apm.plugin.monet.log.cross.wrapper;

import org.apache.skywalking.apm.agent.core.context.ContextSnapshot;

import java.util.function.Function;

/**
 * MonetFunctionWrapper
 * @author FakeBilly
 * @date 2021-12-24 10:24
 * @version V1.0
 * @github https://github.com/fakebilly-dev/monet
 **/
public class MonetFunctionWrapper<T, R> implements Function<T, R> {

    final Function<T, R> function;

    private ContextSnapshot contextSnapshot;

    public MonetFunctionWrapper(Function<T, R> function) {
        this.function = function;
    }

    public MonetFunctionWrapper(Function<T, R> function, ContextSnapshot contextSnapshot) {
        this.function = function;
        this.contextSnapshot = contextSnapshot;
    }

    public static <T, R> MonetFunctionWrapper<T, R> of(Function<T, R> function) {
        return new MonetFunctionWrapper<>(function);
    }

    public static <T, R> MonetFunctionWrapper<T, R> of(Function<T, R> function, ContextSnapshot contextSnapshot) {
        return new MonetFunctionWrapper<>(function, contextSnapshot);
    }


    @Override
    public R apply(T t) {
        return this.function.apply(t);
    }

}
