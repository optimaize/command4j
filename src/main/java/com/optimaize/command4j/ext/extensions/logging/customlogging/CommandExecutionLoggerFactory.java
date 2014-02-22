package com.optimaize.command4j.ext.extensions.logging.customlogging;

import com.optimaize.command4j.Command;
import org.jetbrains.annotations.NotNull;

/**
 * The factory is needed because only at runtime the generic types A and R are known of the command for which
 * logging is performed. (feel free to refactor if you know how to do it without...)
 *
 * @author Fabian Kessler
 */
public interface CommandExecutionLoggerFactory {

    /**
     * @param command Only needed for the generic types.
     * @param <A> The argument.
     * @param <R> The result.
     */
    @NotNull
    <A,R> CommandExecutionLogger<A, R> make(@NotNull Command<A, R> command);
}
