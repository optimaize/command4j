package com.optimaize.command4j.ext.extensions.failover;

import com.optimaize.command4j.Command;
import com.optimaize.command4j.commands.BaseCommand;
import com.optimaize.command4j.ext.extensions.failover.autoretry.AutoRetryExtension;
import com.optimaize.command4j.ext.extensions.failover.autoretry.AutoRetryStrategy;
import org.jetbrains.annotations.NotNull;

/**
 * A hub-class for easy access to built-in failover extensions.
 *
 * @author Fabian Kessler
 */
public class FailoverExtensions {

    public static final FailoverExtensions INSTANCE = new FailoverExtensions();
    private FailoverExtensions(){}



    /**
     * Uses the {@link AutoRetryExtension}.
     * @see AutoRetryExtension
     */
    public static <A, V> BaseCommand<A, V> withAutoRetry(@NotNull Command<A, V> cmd) {
        return new AutoRetryExtension.Interceptor<>(cmd);
    }

    /**
     * @see AutoRetryExtension
     */
    public static <A, V> BaseCommand<A, V> withAutoRetry(@NotNull Command<A, V> cmd,
                                                               @NotNull AutoRetryStrategy strategy) {
        return new AutoRetryExtension.Interceptor<>(cmd, strategy);
    }

}
