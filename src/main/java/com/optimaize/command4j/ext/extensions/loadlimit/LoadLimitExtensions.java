package com.optimaize.command4j.ext.extensions.loadlimit;

import com.optimaize.command4j.Command;
import com.optimaize.command4j.commands.BaseCommand;
import com.optimaize.command4j.ext.extensions.loadlimit.maxconcurrent.MaxConcurrentRunningExtension;
import com.optimaize.command4j.ext.extensions.loadlimit.maxconcurrent.MaxConcurrentState;
import org.jetbrains.annotations.NotNull;

/**
 * A hub-class for easy access to built-in loadlimit extensions.
 *
 * @author Fabian Kessler
 */
public class LoadLimitExtensions {

    public static final LoadLimitExtensions INSTANCE = new LoadLimitExtensions();
    private LoadLimitExtensions(){}


    /**
     * @see MaxConcurrentRunningExtension
     */
    public static <A, V> BaseCommand<A, V> withLoadLimit(@NotNull Command<A, V> cmd, @NotNull MaxConcurrentState strategy) {
        return new MaxConcurrentRunningExtension.Interceptor<>(cmd, strategy);
    }

}
