package com.optimaize.command4j.ext.util.eventbasedextension;

import com.google.common.base.Optional;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.ExecutionContext;
import com.optimaize.command4j.ModeExtension;
import com.optimaize.command4j.commands.BaseCommandInterceptor;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for extensions that don't want to wrap the command themselves. Instead they just
 * implement the event callbacks.
 *
 * @author Fabian Kessler
 */
public abstract class EventBasedExtension implements ModeExtension {

    public static class Interceptor<A, R> extends BaseCommandInterceptor<A, R> {
        private final CommandListener<A,R> commandListener;

        public Interceptor(@NotNull Command<A, R> delegate, @NotNull CommandListener<A,R> commandListener) {
            super(delegate);
            this.commandListener = commandListener;
        }

        @Override
        public R call(@NotNull Optional<A> arg, @NotNull ExecutionContext ec) throws Exception {
            commandListener.before(this, ec, arg);
            R result;
            try {
                result = delegate.call(arg, ec);
            } catch (Exception e) {
                commandListener.afterFailure(this, ec, arg, e);
                throw e;
            }
            commandListener.afterSuccess(this, ec, arg, result);
            return result;
        }
    }
}
