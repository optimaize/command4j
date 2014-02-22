package com.optimaize.command4j.commands;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.ExecutionContext;

import java.util.List;

/**
 * @author Eike Kettner
 */
class CompoundCommand<A, R> extends AbstractCompoundCommand<Iterable<A>, A, R, List<R>> {
    CompoundCommand(Iterable<? extends Command<A, R>> commands) {
        super(commands);
    }

    @Override
    protected List<R> createResult() {
        return Lists.newArrayList();
    }

    @Override
    protected void executeCommand(List<R> result, int i, Command<A, R> cmd, ExecutionContext ec, Optional<Iterable<A>> arg) throws Exception {
        Optional<A> a = Optional.fromNullable(Iterables.get(arg.get(), i));
        result.add(cmd.call(a, ec));
    }

}
