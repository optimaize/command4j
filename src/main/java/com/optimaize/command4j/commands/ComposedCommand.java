package com.optimaize.command4j.commands;

import com.google.common.base.Optional;
import com.optimaize.command4j.ExecutionContext;
import com.optimaize.command4j.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A command that is the composition of two commands.
 *
 * <p>When executing then at first the 1st command gets executed, then its result is fed to the 2nd command, and
 * finally the result of the 2nd command is returned.</p>
 *
 * <p>As soon as an exception occurs (either when executing the first or second command)
 * then the whole thing aborts with the exception.</p>
 *
 * @param <A> the optional Argument's data type for the first command.
 * @param <B> The type of the result of the first command as well as the argument type of the second command.
 * @param <R> the final Result's data type (result of second command).
 * @author Eike Kettner
 * @author Fabian Kessler
 */
final class ComposedCommand<A, B, R> extends BaseCommand<A, R> {

    private final Command<A, B> f;
    private final Command<B, R> g;

    ComposedCommand(Command<A, B> f, Command<B, R> g) {
        this.f = f;
        this.g = g;
    }

    @Override @Nullable
    public R call(@NotNull Optional<A> arg, @NotNull ExecutionContext ec) throws Exception {
        return g.call(Optional.fromNullable(f.call(arg, ec)), ec);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ComposedCommand) {
            ComposedCommand<?, ?, ?> that = (ComposedCommand<?, ?, ?>) obj;
            return f.equals(that.f) && g.equals(that.g);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return f.hashCode() ^ g.hashCode();
    }

    @Override
    public String toString() {
        return g.toString() + "(" + f.toString() + ")";
    }
    @Override
    public String getName() {
        return "Composed("+g.getName() + "(" + f.getName() + "))";
    }
}
