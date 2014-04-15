package com.optimaize.command4j.cache;

import com.optimaize.command4j.lang.Immutable;
import org.jetbrains.annotations.NotNull;

/**
 * The key used for the {@link ExecutorCache}.
 *
 * This class may be subclassed on purpose. Make sure you implement equals/hashCode correctly
 * respecting {@code type}, and that it's immutable.
 *
 * @param <V> The Value's type.
 * @author Fabian Kessler
 */
@Immutable
public class ExecutorCacheKey<V> {

    @NotNull
    protected final Class<V> type;

    public static <V> ExecutorCacheKey<V> create(@NotNull Class<V> type) {
        return new ExecutorCacheKey<>(type);
    }
    protected ExecutorCacheKey(@NotNull Class<V> type) {
        this.type = type;
    }

    @NotNull
    public Class<V> getType() {
        return type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExecutorCacheKey that = (ExecutorCacheKey) o;
        if (!type.equals(that.type)) return false;
        return true;
    }
    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
