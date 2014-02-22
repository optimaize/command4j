package com.optimaize.command4j.ext.util.eventbasedextension;

import com.google.common.base.Optional;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Is informed {@link #before before} and after a {@link #afterSuccess successful} and {@link #afterFailure failed}
 * command execution.
 *
 * @author Fabian Kessler
 */
public interface CommandListener<A,R> {

    public void before(@NotNull Command<A, R> command,
                       @NotNull ExecutionContext ec,
                       @NotNull Optional<A> arg) throws Exception;

    public void afterSuccess(@NotNull Command<A, R> command,
                             @NotNull ExecutionContext ec,
                             @NotNull Optional<A> arg,
                             @Nullable R result) throws Exception;

    public void afterFailure(@NotNull Command<A, R> command,
                             @NotNull ExecutionContext ec,
                             @NotNull Optional<A> arg,
                             @NotNull Exception exception) throws Exception;

}
