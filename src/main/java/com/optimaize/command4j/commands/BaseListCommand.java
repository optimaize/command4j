package com.optimaize.command4j.commands;

import com.optimaize.command4j.Command;

/**
 * Base for commands made up of more than one command.
 *
 * @param <A> the optional Argument's data type
 * @param <R> the Result's data type
 * @author Eike Kettner
 */
public abstract class BaseListCommand<A, R> extends BaseCommand<A, Iterable<R>> {

    /**
     * @see Commands#flatAnd
     */
    public final BaseListCommand<A, R> flatAnd(Command<A, R> cmd) {
        return Commands.flatAnd(this, cmd);
    }
}
