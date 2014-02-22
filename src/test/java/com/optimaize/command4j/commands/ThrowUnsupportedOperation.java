package com.optimaize.command4j.commands;

import com.google.common.base.Optional;
import com.optimaize.command4j.ExecutionContext;
import org.jetbrains.annotations.NotNull;

/**
 * Always throws an UnsupportedOperationException.
 *
 * @author Fabian Kessler
 */
public class ThrowUnsupportedOperation extends BaseCommand<Void, Void> {

    @Override
    public Void call(@NotNull Optional<Void> arg, @NotNull ExecutionContext ec) throws Exception {
        throw new UnsupportedOperationException("Nah, can't do!");
    }

}
