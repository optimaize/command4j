package com.optimaize.command4j.lang;

import org.jetbrains.annotations.NotNull;

/**
 * Provides simple tuple classes, pretty obvious.
 *
 * <p>All values are non-null by definition. Hint: use {@link com.google.common.base.Optional}.</p>
 *
 * <p>Tuples should be avoided when possible. It is usually better to write a meaningful class name.
 * There is no plan to offer great Tuple classes.</p>
 *
 * @author Eike Kettner
 * @author Fabian Kessler
 */
public final class Tuples {

    public static <A, B> Tuple2<A, B> tuple(@NotNull A a, @NotNull B b) {
        return new Tuple2<>(a, b);
    }

    public static <A, B, C> Tuple3<A, B, C> tuple(@NotNull A a, @NotNull B b, @NotNull C c) {
        return new Tuple3<>(a, b, c);
    }
}
