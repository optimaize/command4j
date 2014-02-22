package com.optimaize.command4j.ext.extensions.logging;

import com.optimaize.command4j.Command;
import com.optimaize.command4j.commands.BaseCommand;
import com.optimaize.command4j.ext.extensions.logging.customlogging.CommandExecutionLogger;
import com.optimaize.command4j.ext.extensions.logging.customlogging.CustomLoggingExtension;
import com.optimaize.command4j.ext.extensions.logging.stdoutlogging.StdoutLoggingExtension;
import org.jetbrains.annotations.NotNull;

/**
 * A hub-class for easy access to built-in logging extensions.
 *
 * @author Fabian Kessler
 */
public class LoggingExtensions {

    public static final LoggingExtensions INSTANCE = new LoggingExtensions();
    private LoggingExtensions(){}


    /**
     * @see StdoutLoggingExtension
     */
    public static <A, R> BaseCommand<A, R> withStdoutLogging(@NotNull Command<A, R> cmd) {
        return new StdoutLoggingExtension.Interceptor<>(cmd);
    }

    /**
     * @see CustomLoggingExtension
     */
    public static <A, R> BaseCommand<A, R> withCustomLogging(@NotNull Command<A, R> cmd,
                                                                   @NotNull CommandExecutionLogger<A, R> commandExecutionLogger) {
        return new CustomLoggingExtension.Interceptor<>(cmd, commandExecutionLogger);
    }

}
