package com.optimaize.command4j.ext.util.eventbasedextension;

import com.google.common.base.Optional;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs and swallows exceptions.
 *
 * <p>This works as an exception stopper an does not let any Exception go through. An Error however is not
 * caught and thus goes through.</p>
 *
 * <p>In case the logger throws then its exception is caught and ignored.</p>
 *
 * @author Fabian Kessler
 */
public class ExceptionBarrierCommandListener<A,R> extends CommandListenerWrapper<A, R> {

    private static final Logger defaultLogger = LoggerFactory.getLogger(ExceptionBarrierCommandListener.class);

    @NotNull
    private final Logger logger;

    public ExceptionBarrierCommandListener(@NotNull CommandListener<A, R> commandListener) {
        this(commandListener, defaultLogger);
    }
    public ExceptionBarrierCommandListener(@NotNull CommandListener<A, R> commandListener,
                                           @NotNull Logger logger) {
        super(commandListener);
        this.logger = logger;
    }

    private void log(@NotNull String when, @NotNull Exception e) {
        try {
            //atm we don't try to be smart and log the A arg or R result.
            //if you do then make sure to catch exceptions while making toString() of those
            //separately... maybe those are the causes of the original exception!
            logger.error("Exception when calling a '"+when+"' listener!", e);
        } catch (Exception loggerEx) {
            //ugh. bad. so this one goes unnoticed.
        }
    }


    @Override
    public void before(@NotNull Command<A, R> command,
                       @NotNull ExecutionContext ec,
                       @NotNull Optional<A> arg) throws Exception {
        try {
            wrapped.before(command, ec, arg);
        } catch (Exception e) {
            log("before", e); //log and swallow
        }
    }

    @Override
    public void afterSuccess(@NotNull Command<A, R> command,
                             @NotNull ExecutionContext ec,
                             @NotNull Optional<A> arg,
                             @Nullable R result) throws Exception {
        try {
            wrapped.afterSuccess(command, ec, arg, result);
        } catch (Exception e) {
            log("afterSuccess", e); //log and swallow
        }
    }

    @Override
    public void afterFailure(@NotNull Command<A, R> command,
                             @NotNull ExecutionContext ec,
                             @NotNull Optional<A> arg,
                             @NotNull Exception exception) throws Exception {
        try {
            wrapped.afterFailure(command, ec, arg, exception);
        } catch (Exception e) {
            log("afterFailure", e); //log and swallow
        }
    }
}
