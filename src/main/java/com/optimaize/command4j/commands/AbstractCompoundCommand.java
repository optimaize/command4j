package com.optimaize.command4j.commands;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.ExecutionContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Kettner
 */
abstract class AbstractCompoundCommand<A, B, T, E extends Iterable<T>> extends BaseListCommand<A, T> {
    private static final Joiner joiner = Joiner.on(", ");
    private final Iterable<? extends Command<B, T>> commands;

    public AbstractCompoundCommand(Iterable<? extends Command<B, T>> commands) {
        this.commands = commands;
    }

    @Override
    public E call(@NotNull Optional<A> arg, @NotNull ExecutionContext ec) throws Exception {
        E results = createResult();
        int i = 0;
        for (Command<B, T> task : commands) {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            if (task != null) {
                executeCommand(results, i++, task, ec, arg);
            }
        }
        return results;
    }

    protected abstract E createResult();

    protected abstract void executeCommand(E result, int i, Command<B, T> cmd, ExecutionContext ec, Optional<A> arg) throws Exception;

    // hashCode() & equals() copied from guavas
    @Override
    public int hashCode() {
        int result = -1; /* Start with all bits on. */
        for (Command<?, ?> task : commands) {
            result &= task.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractCompoundCommand) {
            AbstractCompoundCommand that = (AbstractCompoundCommand) obj;
            Iterables.elementsEqual(commands, that.commands);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Compound(" + joiner.join(commands) + ")";
    }
    @Override
    public String getName() {
        List<String> names = new ArrayList<>();
        for (Command<B, T> command : commands) {
            names.add(command.getName());
        }
        return "Compound(" + joiner.join(names) + ")";
    }
}
