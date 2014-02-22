package com.optimaize.command4j.commands;

import com.google.common.base.Optional;
import com.optimaize.command4j.ExecutionContext;
import org.jetbrains.annotations.NotNull;

/**
 * Returns the input.
 *
 * @author Fabian Kessler
 */
public class ReturnInputString extends BaseCommand<String, String> {

    @Override
    public String call(@NotNull Optional<String> arg, @NotNull ExecutionContext ec) throws Exception {
        return arg.get();
    }

}
