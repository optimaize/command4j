package com.optimaize.command4j.commands;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.ExecutionContext;

import java.util.List;

/**
 * @author Eike Kettner
 */
class AndCommand<A, R> extends AbstractCompoundCommand<A, A, R, List<R>> {

    public AndCommand(Iterable<? extends Command<A, R>> commands) {
        super(commands);
    }

    @Override
    protected List<R> createResult() {
        return Lists.newArrayList();
    }

    @Override
    protected void executeCommand(List<R> result, int i, Command<A, R> cmd, ExecutionContext ec, Optional<A> arg) throws Exception {
        result.add(cmd.call(arg, ec));
    }

    @Override
    public String toString() {
        return "And(" + super.toString() + ")";
    }
    @Override
    public String getName() {
        return "And(" + super.getName() + ")";
    }
}
