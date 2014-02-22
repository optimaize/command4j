package com.optimaize.command4j.commands;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Executes the first, and if the result matches the condition then the result is returned.
 * Otherwise the second is executed and its result is returned.
 *
 * @param <A> the optional Argument's data type
 * @param <R> the Result's data type
 * @author Eike Kettner
 * @author Fabian Kessler
 */
final class ConditionCommand<A, R> extends BaseCommand<A, R> {

    @NotNull
    private final Command<A, R> primary;
    @NotNull
    private final Command<A, R> secondary;
    @NotNull
    private final Predicate<R> condition;

    ConditionCommand(@NotNull Command<A, R> primary, @NotNull Command<A, R> secondary, @NotNull Predicate<R> condition) {
        this.primary = primary;
        this.secondary = secondary;
        this.condition = condition;
    }

    @Override @Nullable
    public R call(@NotNull Optional<A> arg, @NotNull ExecutionContext ec) throws Exception {
        R pr = primary.call(arg, ec);
        if (condition.apply(pr)) {
            return pr;
        } else {
            return secondary.call(arg, ec);
        }
    }
}
