package com.optimaize.command4j.ext.extensions.logging.customlogging;

import com.google.common.base.Optional;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

/**
 * You are encouraged to override the methods to change the logging.
 * You may also override the {@link #before} method with an empty one to not log before at all.
 *
 * @author Fabian Kessler
 */
public class CommandExecutionLoggerImpl<A,R> implements CommandExecutionLogger<A, R> {

    @NotNull
    private final Logger logger;

    public CommandExecutionLoggerImpl(@NotNull Logger logger) {
        this.logger = logger;
    }


    @Override
    public void before(@NotNull Command<A, R> command, @NotNull ExecutionContext ec, @NotNull Optional<A> arg) {
        logger.info("Before {0} with arg {1}", command, arg);
    }

    @Override
    public void afterSuccess(@NotNull Command<A, R> command, @NotNull ExecutionContext ec, @NotNull Optional<A> arg, @Nullable R result) {
        logger.info("After success {0} result: {1}", command, result);
    }

    @Override
    public void afterFailure(@NotNull Command<A, R> command, @NotNull ExecutionContext ec, @NotNull Optional<A> arg, @NotNull Exception exception) {
        logger.info("After failure {0} exception: {1}", command, exception);
    }
}
