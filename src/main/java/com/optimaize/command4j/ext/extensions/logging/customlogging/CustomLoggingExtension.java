package com.optimaize.command4j.ext.extensions.logging.customlogging;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.ExecutionContext;
import com.optimaize.command4j.Mode;
import com.optimaize.command4j.ModeExtension;
import com.optimaize.command4j.commands.BaseCommandInterceptor;
import com.optimaize.command4j.ext.util.eventbasedextension.CommandListener;
import com.optimaize.command4j.lang.Key;
import org.jetbrains.annotations.NotNull;

/**
 * Calls your logger before and after the method.
 *
 * <p>Configuration:
 * <pre><code>
 *     .with(CustomLoggingExtension.LOGGER, new CommandExecutionLoggerFactoryImpl(logger))
 * </code></pre>
 * </p>
 *
 * @author Fabian Kessler
 */
public class CustomLoggingExtension implements ModeExtension {

    public static final Key<CommandExecutionLoggerFactory> LOGGER = Key.create(
            "CustomLoggingExtension.customLogging",
            CommandExecutionLoggerFactory.class
    );


    @NotNull @Override
    public <A, R> Command<A, R> extend(@NotNull final Command<A, R> cmd, @NotNull Mode mode) {
        return mode.get(LOGGER).transform(new Function<CommandExecutionLoggerFactory, Command<A, R>>() {
            @Override
            public Command<A, R> apply(CommandExecutionLoggerFactory input) {
                if (input != null) {
                    return new Interceptor<>(cmd, input.make(cmd));
                }
                return cmd;
            }
        }).or(cmd);
    }

    public static class Interceptor<A, R> extends BaseCommandInterceptor<A, R> {
        private final CommandListener<A,R> commandExecutionLogger;

        public Interceptor(@NotNull Command<A, R> delegate, @NotNull CommandListener<A,R> commandExecutionLogger) {
            super(delegate);
            this.commandExecutionLogger = commandExecutionLogger;
        }

        @Override
        public R call(@NotNull Optional<A> arg, @NotNull ExecutionContext ec) throws Exception {
            commandExecutionLogger.before(this, ec, arg);
            R result;
            try {
                result = delegate.call(arg, ec);
            } catch (Exception e) {
                commandExecutionLogger.afterFailure(this, ec, arg, e);
                throw e;
            }
            commandExecutionLogger.afterSuccess(this, ec, arg, result);
            return result;
        }

        @Override
        public String toString() {
            return "LoggingInterceptor{" +
                    "delegate=" + delegate +
                    '}';
        }
    }
}
