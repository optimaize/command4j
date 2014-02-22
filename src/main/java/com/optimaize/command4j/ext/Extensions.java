package com.optimaize.command4j.ext;

import com.optimaize.command4j.ext.extensions.exception.ExceptionExtensions;
import com.optimaize.command4j.ext.extensions.failover.FailoverExtensions;
import com.optimaize.command4j.ext.extensions.logging.LoggingExtensions;
import com.optimaize.command4j.ext.extensions.timeout.TimeoutExtensions;
import org.jetbrains.annotations.NotNull;

/**
 * A hub-class for easy access to common built-in extensions.
 *
 * @author Fabian Kessler
 */
public class Extensions {

    public static final Extensions INSTANCE = new Extensions();
    private Extensions(){}


    @NotNull
    public static LoggingExtensions logging() {
        return LoggingExtensions.INSTANCE;
    }

    @NotNull
    public static ExceptionExtensions exception() {
        return ExceptionExtensions.INSTANCE;
    }

    @NotNull
    public static TimeoutExtensions timeout() {
        return TimeoutExtensions.INSTANCE;
    }

    @NotNull
    public static FailoverExtensions failover() {
        return FailoverExtensions.INSTANCE;
    }

}
