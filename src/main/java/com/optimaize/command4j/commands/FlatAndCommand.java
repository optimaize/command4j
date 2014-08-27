package com.optimaize.command4j.commands;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.ExecutionContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @see Commands#flatAnd
 * @author Eike Kettner
 */
class FlatAndCommand<A, R> extends BaseListCommand<A, R> {
    @NotNull
    private final Command<A, ? extends Iterable<R>> first;
    @NotNull
    private final Command<A, R> second;

    FlatAndCommand(@NotNull Command<A, ? extends Iterable<R>> first, @NotNull Command<A, R> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public List<R> call(@NotNull Optional<A> arg, @NotNull ExecutionContext ec) throws Exception {
        List<R> result = Lists.newArrayList();
        Iterables.addAll(result, first.call(arg, ec));
        result.add(second.call(arg, ec));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlatAndCommand that = (FlatAndCommand) o;

        if (!first.equals(that.first)) return false;
        if (!second.equals(that.second)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FlatAnd(" + first + ", " + second + ")";
    }

    @Override
    public String getName() {
        return "FlatAnd(" + first.getName() + "/" + second.getName() + ")";
    }

}
