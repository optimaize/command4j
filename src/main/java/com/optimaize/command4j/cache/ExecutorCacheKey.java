package com.optimaize.command4j.cache;

import com.optimaize.command4j.lang.Immutable;
import org.jetbrains.annotations.NotNull;

/**
 * The key used for the {@link ExecutorCache}.
 *
 * @param <V> The Value's type.
 * @author Fabian Kessler
 */
@Immutable
public class ExecutorCacheKey<V> {

    @NotNull
    protected final Class<V> type;

    public ExecutorCacheKey(@NotNull Class<V> type) {
        this.type = type;
    }

    @NotNull
    public Class<V> getType() {
        return type;
    }
}
