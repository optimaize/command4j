package com.optimaize.command4j.commands;

import com.google.common.base.Optional;
import com.optimaize.command4j.ExecutionContext;
import org.jetbrains.annotations.NotNull;

/**
 * Sleeps a bit and then returns.
 *
 * @author Fabian Kessler
 */
public class Sleep extends BaseCommand<Void, Void> {

    private final long sleepMs;
    public Sleep(long sleepMs) {
        this.sleepMs = sleepMs;
    }

    @Override
    public Void call(@NotNull Optional<Void> arg, @NotNull ExecutionContext ec) throws Exception {
        Thread.sleep(sleepMs);
        return null;
    }

}
