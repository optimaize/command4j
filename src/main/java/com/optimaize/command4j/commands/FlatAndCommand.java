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
    private final Command<A, ? extends Iterable<R>> first;
    private final Command<A, R> second;

    FlatAndCommand(Command<A, ? extends Iterable<R>> first, Command<A, R> second) {
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
    public String toString() {
        return "FlatAnd(" + first + ", " + second + ")";
    }
}
