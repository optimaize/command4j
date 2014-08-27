package com.optimaize.command4j.commands;

import com.google.common.base.Optional;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.ExecutionContext;
import com.optimaize.command4j.Mode;
import com.optimaize.command4j.lang.Key;
import org.jetbrains.annotations.NotNull;

/**
 * @author Eike Kettner
 */
class SetValueCommand<A, R, V> extends BaseCommand<A, R> {
    private final Command<A, R> delegate;
    private final Key<V> key;
    private final V value;

    SetValueCommand(Command<A, R> delegate, Key<V> key, V value) {
        this.delegate = delegate;
        this.key = key;
        this.value = value;
    }

    @Override
    public R call(@NotNull Optional<A> arg, @NotNull ExecutionContext ec) throws Exception {
        Mode m = ec.getMode().with(key, value);
        return ec.execute(delegate, m, arg).orNull();
    }

    @Override
    public String toString() {
        return "SetValue(" + delegate + "<<=" + key + "=" + value + ")";
    }

    @Override
    public String getName() {
        return "SetValue(" + delegate.getName()+")";
    }

}
