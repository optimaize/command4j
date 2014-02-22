package com.optimaize.command4j.impl;

import com.google.common.base.Optional;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.CommandExecutor;
import com.optimaize.command4j.CommandExecutorService;
import com.optimaize.command4j.Mode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutorService;

/**
 * Base for {@link CommandExecutor} wrappers.
 *
 * TODO explain when this is useful, add an example.
 *
 * @author Fabian Kessler
 */
public abstract class CommandExecutorWrapper implements CommandExecutor {

    /*
     * Impl note:
     * This is using the wrapped() method instead of a field so that context based services
     * (such as spring services) can use this where the actual implementation is only found
     * out late, after the constructor (think @Inject or @PostConstruct).
     */


    @NotNull
    protected abstract CommandExecutor wrapped();

    @Override @NotNull
    public <A, R> Optional<R> execute(@NotNull Command<A, R> cmd, @NotNull Mode mode, @Nullable A arg) throws Exception {
        return wrapped().execute(cmd, mode, arg);
    }

    @Override @NotNull
    public CommandExecutorService service(@NotNull ExecutorService executorService) {
        return wrapped().service(executorService);
    }

    @Override @NotNull
    public CommandExecutorService service() {
        return wrapped().service();
    }
}
