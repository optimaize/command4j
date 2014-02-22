package com.optimaize.command4j.commands;

import com.google.common.base.Optional;
import com.optimaize.command4j.ExecutionContext;
import org.jetbrains.annotations.NotNull;

/**
 * Always returns null.
 *
 * @author Fabian Kessler
 */
public class ReturnNull extends BaseCommand<Void, Void> {

    @Override
    public Void call(@NotNull Optional<Void> arg, @NotNull ExecutionContext ec) throws Exception {
        return null;
    }

}
