package com.optimaize.command4j.commands;

import com.google.common.base.Optional;
import com.optimaize.command4j.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Always returns the string passed in the constructor.
 *
 * @author Fabian Kessler
 */
public class ReturnConstructorString extends BaseCommand<Void, String> {

    @Nullable
    private final String string;
    public ReturnConstructorString(@Nullable String string) {
        this.string = string;
    }

    @Override
    public String call(@NotNull Optional<Void> arg, @NotNull ExecutionContext ec) throws Exception {
        return string;
    }

}
