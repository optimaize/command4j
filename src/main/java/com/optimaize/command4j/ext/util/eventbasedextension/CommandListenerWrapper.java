package com.optimaize.command4j.ext.util.eventbasedextension;

import com.google.common.base.Optional;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Fabian Kessler
 */
public class CommandListenerWrapper<A,R> implements CommandListener<A, R> {

    @NotNull
    protected final CommandListener<A, R> wrapped;

    public CommandListenerWrapper(@NotNull CommandListener<A, R> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void before(@NotNull Command<A, R> command, @NotNull ExecutionContext ec, @NotNull Optional<A> arg) throws Exception {
        wrapped.before(command, ec, arg);
    }

    @Override
    public void afterSuccess(@NotNull Command<A, R> command, @NotNull ExecutionContext ec, @NotNull Optional<A> arg, @Nullable R result) throws Exception {
        wrapped.afterSuccess(command, ec, arg, result);
    }

    @Override
    public void afterFailure(@NotNull Command<A, R> command, @NotNull ExecutionContext ec, @NotNull Optional<A> arg, @NotNull Exception exception) throws Exception {
        wrapped.afterFailure(command, ec, arg, exception);
    }
}
