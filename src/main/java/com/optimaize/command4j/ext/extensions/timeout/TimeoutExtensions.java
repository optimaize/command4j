package com.optimaize.command4j.ext.extensions.timeout;

import com.optimaize.command4j.Command;
import com.optimaize.command4j.commands.BaseCommand;
import com.optimaize.command4j.ext.extensions.timeout.configurabletimeout.TimeoutExtension;
import com.optimaize.command4j.lang.Duration;
import org.jetbrains.annotations.NotNull;

/**
 * A hub-class for easy access to built-in timeout extensions.
 *
 * @author Fabian Kessler
 */
public class TimeoutExtensions {

    public static final TimeoutExtensions INSTANCE = new TimeoutExtensions();
    private TimeoutExtensions(){}


    /**
     * @see TimeoutExtension
     */
    public static <A, V> BaseCommand<A, V> withTimeout(@NotNull Command<A, V> cmd, @NotNull Duration duration) {
        return new TimeoutExtension.Interceptor<>(cmd, duration);
    }

}
