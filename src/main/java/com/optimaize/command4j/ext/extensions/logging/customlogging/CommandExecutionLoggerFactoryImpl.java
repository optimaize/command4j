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

    public CommandExecutionLoggerFactoryImpl(@NotNull Logger logger) {
        this.logger = logger;
    }

    @Override @NotNull
    public <A,R> CommandExecutionLogger<A, R> make(@NotNull Command<A, R> command) {
        return new CommandExecutionLoggerImpl<>(logger);
    }

}
