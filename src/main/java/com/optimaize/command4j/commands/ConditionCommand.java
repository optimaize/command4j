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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConditionCommand that = (ConditionCommand) o;

        if (!condition.equals(that.condition)) return false;
        if (!primary.equals(that.primary)) return false;
        if (!secondary.equals(that.secondary)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = primary.hashCode();
        result = 31 * result + secondary.hashCode();
        result = 31 * result + condition.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ConditionCommand{" +
                "primary=" + primary +
                ", secondary=" + secondary +
                ", condition=" + condition +
                '}';
    }

    @Override
    public String getName() {
        return "Condition(" +primary.getName() + "/" + secondary.getName()+")";
    }
}
