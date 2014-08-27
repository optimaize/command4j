package com.optimaize.command4j.ext.extensions.logging.customlogging;

import com.optimaize.command4j.Command;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/**
 * You are encouraged to create your own and return a different logger implementation.
 *
 * @author Fabian Kessler
 */
public class CommandExecutionLoggerFactoryImpl implements CommandExecutionLoggerFactory {

    @NotNull
    private final Logger logger;
    private final boolean logArgumentInResult;

    /**
     * Uses logArgumentInResult=false.
     */
    public CommandExecutionLoggerFactoryImpl(@NotNull Logger logger) {
        this(logger, false);
    }

    /**
     * @param logArgumentInResult Optionally writes the argument with the result line again.
     *                            This is useful when either you only log results (and not requests), or you want it more convenient and don't mind generating larger log files.
     */
    public CommandExecutionLoggerFactoryImpl(@NotNull Logger logger, boolean logArgumentInResult) {
        this.logger = logger;
        this.logArgumentInResult = logArgumentInResult;
    }

    @Override @NotNull
    public <A,R> CommandExecutionLogger<A, R> make(@NotNull Command<A, R> command) {
        return new CommandExecutionLoggerImpl<>(logger, logArgumentInResult);
    }

}
