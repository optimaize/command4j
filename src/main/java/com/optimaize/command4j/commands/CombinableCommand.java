package com.optimaize.command4j.commands;

import com.google.common.base.Predicate;
import com.optimaize.command4j.Command;
import org.jetbrains.annotations.NotNull;

/**
 * Extends the {@link Command} interface with combinator methods for command chaining and such.
 *
 * @author Eike Kettner
 * @author Fabian Kessler
 */
public interface CombinableCommand<A, R> extends Command<A, R> {

    /**
     * Returns a new command that is the composition of this and the given
     * command; thus executing at first this command and then {@code cmd}
     * using the result of this as its argument.
     *
     * <p>As soon as an exception occurs (either when executing the first or second command)
     * then the whole thing aborts with the exception.</p>
     */
    @NotNull
    <C> CombinableCommand<A, C> andThen(@NotNull Command<R, C> cmd);

    /**
     * The returned command executes the first, and if the condition applied on its result
     * is true then the result is returned. Otherwise the <code>alternative</code> command
     * is executed, and its result is returned (no condition checking there).
     * @see Commands#firstIfTrue
     */
    @NotNull
    CombinableCommand<A, R> ifTrueOr(Predicate<R> cond, Command<A, R> alternative);

    /**
     * The returned command executes the first, and if the result is not null then
     * the result is returned. Otherwise the <code>alternative</code> command
     * is executed, and its result is returned (even if it's null).
     */
    @NotNull
    CombinableCommand<A, R> ifNotNullOr(Command<A, R> alternative);

    /**
     * @see Commands#and(Command, Command)
     */
    @NotNull
    BaseListCommand<A, R> and(Command<A, R> cmd);

    /**
     * @see Commands#concat(Command, Command)
     */
    @NotNull
    BaseListCommand<Iterable<A>, R> concat(Command<A, R> cmd);
}
