package com.optimaize.command4j.commands;

import com.google.common.base.Optional;
import com.optimaize.command4j.ExecutionContext;
import org.jetbrains.annotations.NotNull;

/**
 * Multiplies numbers.
 *
 * @author Fabian Kessler
 */
public class Multiply extends BaseCommand<Long, Long> {

    private final long multiplier;
    public Multiply(long multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public Long call(@NotNull Optional<Long> arg, @NotNull ExecutionContext ec) throws Exception {
        return multiplier * arg.get();
    }

}
