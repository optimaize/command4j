package com.optimaize.command4j;

import com.google.common.base.Optional;
import com.optimaize.command4j.lang.Tuples;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The global contract of a command. They can get an optional argument A and
 * return some result R. The optional argument must aggregate all arguments
 * needed for the service call.
 *
 * <p>More "global" arguments like context information (such as an api-key
 * for a remote procedure call), are defined with the {@link Mode} class for
 * each execution. They are then also available to all nested tasks that may
 * be executed on behalf of another command.</p>
 *
 * <p>The argument/result specification allows for chaining commands.
 * Execute command 1, take the result, feed it to command 2, return (and such).</p>
 *
 * @param <A> the optional argument's data type
 * @param <R> the result's data type
 *
 * @author Eike Kettner
 * @author Fabian Kessler
 */
public interface Command<A, R> {

    /**
     * Runs the remote command.
     *
     * <p>This method always blocks until either the command finishes successfully, aborts by throwing an
     * exception, or is cancelled early. (The {@link CommandExecutorService} allows you to execute
     * commands in a non-blocking way.)</p>
     *
     * @param arg The {@link Optional} argument. If you need more than 1 argument then you may create a
     *            request class as container, or maybe it's enough to just use {@link Tuples tuples}.
     * @param ec
     * @return Commands may return null, since null values are handled by the framework in that they're wrapped
     *         into an {@link Optional}.
     * @throws Exception
     *
     * TODO Eike: The return value is nullable and the framework makes an Optional out of it.
     *            Why not the same for the argument? We could make the interface more consistent.
     *            Is there a reason against that?
     */
    @Nullable
    R call(@NotNull Optional<A> arg, @NotNull ExecutionContext ec) throws Exception;

    /**
     * Like Java's Thread, a Command has a name.
     *
     * <p>Because toString() can become long with all the interceptors wrapping a base command, this
     * keeps it simple to what it actually does.</p>
     *
     * <p>Certain kinds of combined commands, like a ComposedCommand or a ConditionCommand, don't have a single
     * name, they show the names of the contained commands.</p>
     *
     * @return an identifier, not unique but usually distinguishable
     */
    String getName();
}
