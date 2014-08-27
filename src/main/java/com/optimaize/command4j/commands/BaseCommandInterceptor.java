package com.optimaize.command4j.commands;

import com.optimaize.command4j.Command;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for interceptors.
 *
 * @author aa
 */
public abstract class BaseCommandInterceptor<A, R> extends BaseCommand<A, R> {

    @NotNull
    protected final Command<A, R> delegate;

    protected BaseCommandInterceptor(@NotNull Command<A, R> delegate) {
        this.delegate = delegate;
    }

    /**
     * @return The name of the intercepted command.
     */
    @Override
    public String getName() {
        return delegate.getName();
    }

}

