package com.optimaize.command4j.ext.extensions.logging.stdoutlogging;

import com.google.common.base.Optional;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.ExecutionContext;
import com.optimaize.command4j.Mode;
import com.optimaize.command4j.ModeExtension;
import com.optimaize.command4j.commands.BaseCommandInterceptor;
import com.optimaize.command4j.lang.Key;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is a very simple logger that writes to stdout before and after executing a command.
 *
 * <p>Obviously this is intended for the first development steps only.</p>
 *
 * <p>Configuration:
 * <pre><code>
 *     .with(StdoutLoggingExtension.enabled())
 * </code></pre>
 * </p>
 *
 * @author Eike Kettner
 */
public class StdoutLoggingExtension implements ModeExtension {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm:ss:SSS");

    /**
     * If this is true, a short info is printed to stdout before and
     * after executing each command.
     */
    public static final Key<Boolean> STDOUT_LOGGING_ENABLED = Key.booleanKey("StdoutLoggingExtension.enabled");

    @NotNull
    public static Key<Boolean> enabled() {
        return STDOUT_LOGGING_ENABLED;
    }

    @NotNull @Override
    public <A, R> Command<A, R> extend(@NotNull Command<A, R> cmd, @NotNull Mode mode) {
        if (mode.is(STDOUT_LOGGING_ENABLED)) {
            return new Interceptor<>(cmd);
        }
        return cmd;
    }

    public static class Interceptor<A, R> extends BaseCommandInterceptor<A, R> {

        public Interceptor(@NotNull Command<A, R> delegate) {
            super(delegate);
        }

        @Override
        public R call(@NotNull Optional<A> arg, @NotNull ExecutionContext ec) throws Exception {
            System.out.println(">> "+ now() +" BEFORE " + delegate.toString() + " with arg "+arg);
            R result;
            try {
                result = delegate.call(arg, ec);
            } catch (Exception e) {
                System.out.println(">> "+ now() +" AFTER FAILURE " + delegate.toString() + " exception: " + e);
                throw e;
            }
            System.out.println(">> "+ now() +" AFTER SUCCESS " + delegate.toString() + " result: " + result);
            return result;
        }
    }

    private static String now() {
        return simpleDateFormat.format(new Date());
    }
}
