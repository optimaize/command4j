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
    private final boolean logArgumentInResult;

    /**
     * Uses logArgumentInResult=false.
     */
    public CommandExecutionLoggerImpl(@NotNull Logger logger) {
        this(logger, false);
    }

    /**
     * @param logArgumentInResult Optionally writes the argument with the result line again.
     *                            This is useful when either you only log results (and not requests), or you want it more convenient and don't mind generating larger log files.
     */
    public CommandExecutionLoggerImpl(@NotNull Logger logger, boolean logArgumentInResult) {
        this.logger = logger;
        this.logArgumentInResult = logArgumentInResult;
    }


    @Override
    public void before(@NotNull Command<A, R> command, @NotNull ExecutionContext ec, @NotNull Optional<A> arg) {
        logger.info("Before {} with arg {}", command.getName(), arg);
    }

    @Override
    public void afterSuccess(@NotNull Command<A, R> command, @NotNull ExecutionContext ec, @NotNull Optional<A> arg, @Nullable R result) {
        if (logArgumentInResult) {
            logger.info("After success {} for arg {} got result: {}", new Object[]{command.getName(), arg, result});
        } else {
            logger.info("After success {} result: {}", command.getName(), result);
        }
    }

    @Override
    public void afterFailure(@NotNull Command<A, R> command, @NotNull ExecutionContext ec, @NotNull Optional<A> arg, @NotNull Exception exception) {
        if (logArgumentInResult) {
            logger.info("After failure {} for arg {} got exception: {}", new Object[]{command.getName(), arg, exception});
        } else {
            logger.info("After failure {} exception: {}", command.getName(), exception);
        }
    }
}
