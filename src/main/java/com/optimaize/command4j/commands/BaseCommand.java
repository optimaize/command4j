package com.optimaize.command4j.commands;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.lang.Key;
import org.jetbrains.annotations.NotNull;

/**
 * A base class for users wanting to implement a {@link Command}.
 *
 * @param <A> the optional Argument's data type
 * @param <R> the Result's data type
 * @author Eike Kettner
 */
public abstract class BaseCommand<A, R> implements CombinableCommand<A,R> {

    @NotNull @Override
    public final <C> BaseCommand<A, C> andThen(@NotNull Command<R, C> cmd) {
        return new ComposedCommand<>(this, cmd);
    }

    @NotNull @Override
    public final BaseCommand<A, R> ifTrueOr(Predicate<R> cond, Command<A, R> alternative) {
        return Commands.firstIfTrue(this, cond, alternative);
    }

    @NotNull @Override
    public final BaseCommand<A, R> ifNotNullOr(Command<A, R> alternative) {
        return Commands.firstIfTrue(this, Predicates.<R>notNull(), alternative);
    }

    @NotNull @Override
    public final BaseListCommand<A, R> and(Command<A, R> cmd) {
        return Commands.and(this, cmd);
    }

    @NotNull @Override
    public final BaseListCommand<Iterable<A>, R> concat(Command<A, R> cmd) {
        return Commands.concat(this, cmd);
    }

    /**
     * @see Commands#withValue(Command, Key, Object)
     */
    public final <V> BaseCommand<A, R> withValue(@NotNull Key<V> key, V value) {
        return Commands.withValue(this, key, value);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
