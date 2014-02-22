package com.optimaize.command4j.commands;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.lang.Key;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A hub-class for easy access to common command combinators for command chaining and such.
 *
 * @author Eike Kettner
 * @author Fabian Kessler
 */
public final class Commands {

    private Commands(){}


    /**
     * Returns a command that will return the result of this command
     * if the result applies to the given condition, or otherwise, the
     * result of the alternative command (the condition is not applied there).
     *
     * <p>If any of the two commands throws then the whole thing aborts with the exception.</p>
     */
    public static <A, B> BaseCommand<A, B> firstIfTrue(Command<A, B> first, Predicate<B> cond, Command<A, B> secondary) {
        return new ConditionCommand<>(first, secondary, cond);
    }

    /**
     * Returns a task that executes both tasks sequentially, applying the same argument on both.
     * The result of the execution is an ArrayList containing two elements (0 and 1).
     */
    public static <A, B> BaseListCommand<A, B> and(Command<A, B> first, Command<A, B> second) {
        List<Command<A, B>> list = Lists.newArrayList();
        list.add(first);
        list.add(second);
        return new AndCommand<>(list);
    }

    /**
     * Returns a task that executes all tasks sequentially applying the same argument
     * to each. The result of the execution is an ArrayList containing one element per command
     * in the order the commands were specified here.
     */
    public static <A, B> BaseListCommand<A, B> and(Iterable<? extends Command<A, B>> commands) {
        return new AndCommand<>(commands);
    }

    /**
     * Creates a new task that will append the result of the second task to the result-list
     * of the first.
     * TODO Fabian: test behavior and explain here.
     */
    public static <A, B> BaseListCommand<A, B> flatAnd(Command<A, ? extends Iterable<B>> first, Command<A, B> second) {
        return new FlatAndCommand<>(first, second);
    }

    /**
     * Creates a task that will set the given key-value pair into the mode prior to
     * executing {@code cmd}.
     */
    public static <A, B, V> BaseCommand<A, B> withValue(@NotNull Command<A, B> cmd, Key<V> key, V value) {
        return new SetValueCommand<>(cmd, key, value);
    }

    /**
     * Concatenates commands to one that will run the list sequentially storing each result in
     * a list. The argument array is mapped to the command list by position (the first argument
     * goes to the first command, the second argument to the second command ...).
     *
     * <p>Thus, when executing the command, the argument must have exactly as many elements as
     * there are commands. If you pass in less, an ArrayIndexOutOfBoundsException is thrown.
     * If you pass in more, then as of now the execution works; the additional items are not seen
     * because the command iteration stops before getting there. However, maybe a future version
     * will detect this mismatch and throw. By definition the sizes must match.</p>
     */
    @NotNull
    public static <A, B> BaseListCommand<Iterable<A>, B> concat(@NotNull Iterable<? extends Command<A, B>> commands) {
        return new CompoundCommand<>(commands);
    }

    /**
     * @see #concat(Iterable)
     */
    @NotNull
    public static <A, B> BaseListCommand<Iterable<A>, B> concat(@NotNull Command<A, B> cmd1, @NotNull Command<A, B> cmd2) {
        List<Command<A, B>> list = Lists.newArrayList();
        list.add(cmd1);
        list.add(cmd2);
        return concat(list);
    }


    // simple wrappers/interceptors go here


}
