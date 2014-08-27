package com.optimaize.command4j.ext.extensions.exception.exceptiontranslation;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.ExecutionContext;
import com.optimaize.command4j.Mode;
import com.optimaize.command4j.ModeExtension;
import com.optimaize.command4j.commands.BaseCommandInterceptor;
import com.optimaize.command4j.lang.Key;
import org.jetbrains.annotations.NotNull;

/**
 * Translates exceptions from one to another kind.
 *
 * <p>One use case is to map exceptions thrown by the server to nicer exceptions
 * defined on the client. Otherwise the default SOAPFaultException gets thrown
 * at the user, or an exception class that was auto-generated based on wsdl and
 * isn't very nice either (no javadoc in it).
 * So this kind of translation doesn't really change the type, it just makes
 * it nicer for the developer who has to deal with the exception.</p>
 *
 * <p>The other use case obviously is to really translate the exception from
 * one thing to another.</p>
 *
 * <p>See ExceptionTranslator for the thing that actually performs the translation.</p>
 *
 * <p>Configuration:
 * <pre><code>
 *     .with(ExceptionTranslationExtension.TRANSLATOR, new MyExceptionTranslator())
 * </code></pre>
 * </p>
 *
 * @author Fabian Kessler
 */
public class ExceptionTranslationExtension implements ModeExtension {

    /**
     */
    @SuppressWarnings("unchecked")
    public static final Key<ExceptionTranslator> TRANSLATOR = Key.create(
            "ExceptionTranslationExtension.translator",
            ExceptionTranslator.class
    );

    @NotNull @Override
    public <A, R> Command<A, R> extend(@NotNull final Command<A, R> cmd, @NotNull Mode mode) {
        return mode.get(TRANSLATOR).transform(new Function<ExceptionTranslator, Command<A, R>>() {
            @Override
            public Command<A, R> apply(ExceptionTranslator input) {
                if (input != null) {
                    return new Interceptor<>(cmd, input);
                }
                return cmd;
            }
        }).or(cmd);
    }

    public static class Interceptor<A, R> extends BaseCommandInterceptor<A, R> {
        private final ExceptionTranslator exceptionTranslator;

        public Interceptor(@NotNull Command<A, R> delegate, @NotNull ExceptionTranslator exceptionTranslator) {
            super(delegate);
            this.exceptionTranslator = exceptionTranslator;
        }

        @Override
        public R call(@NotNull Optional<A> arg, @NotNull ExecutionContext ec) throws Exception {
            try {
                return delegate.call(arg, ec);
            } catch (Throwable t) {
                if (t.getClass() == InterruptedException.class) {
                    //we have 2 possibilities here.
                    //1. either allow the translator to possibly translate it, but then if it does we need
                    //   to reset the interrupted flag using:
                    //   Thread.currentThread().interrupt();
                    //2. or don't allow any translation of this at all.
                    //   we're going with this, we don't see any use ever of translating this.
                    throw t;
                }
                if (exceptionTranslator.canTranslate(t)) {
                    throw exceptionTranslator.translate(t);
                }
                if (t instanceof Exception) {
                    throw (Exception) t;
                }
                throw Throwables.propagate(t);
            }
        }


        @Override
        public String toString() {
            return "ExceptionTranslation(" + delegate.toString() + ")";
        }
    }
}
