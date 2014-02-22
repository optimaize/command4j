package com.optimaize.command4j.cache;

import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.UncheckedExecutionException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * This mimics the {@link com.google.common.cache.Cache} interface
 * for use with {@link ExecutorCacheKey}s.
 *
 * <p>This cache can be used by commands to store creation-expensive
 * objects that can live for the whole application life-time. The cache
 * is instantiated once with the {@link com.optimaize.command4j.CommandExecutor} and all commands
 * can store their objects using the {@link com.optimaize.command4j.ExecutionContext}'s accessor.</p>
 *
 * @author Eike Kettner
 * @author Fabian Kessler
 */
public final class ExecutorCache {

    private final Cache<ExecutorCacheKey, Object> cache;

    public ExecutorCache(@NotNull Cache<ExecutorCacheKey, Object> cache) {
        this.cache = cache;
    }

    public ExecutorCache() {
        this.cache = CacheBuilder
                .newBuilder()
                .softValues()
                .maximumSize(500)
                .weakKeys()
                .build();
    }

    @Nullable
    public <V> V getIfPresent(@NotNull ExecutorCacheKey<V> key) {
        //noinspection unchecked
        return (V) cache.getIfPresent(key);
    }

    public <V> V get(@NotNull ExecutorCacheKey<V> key, @NotNull Callable<? extends V> valueLoader) throws Exception {
        //to understand this, see what cache.get() throws. we want to unwrap the throwable, whatever it was.
        try {
            //noinspection unchecked
            return (V) cache.get(key, valueLoader);
        } catch (UncheckedExecutionException | ExecutionError e) {
            throw Throwables.propagate(e.getCause());
        } catch (ExecutionException e) {
            Throwables.propagateIfInstanceOf(e.getCause(), Exception.class);
            throw Throwables.propagate(e.getCause());
        }
    }

    public <V> void put(@NotNull ExecutorCacheKey<V> key, V value) {
        cache.put(key, value);
    }

    public void invalidate(@NotNull ExecutorCacheKey<?> key) {
        cache.invalidate(key);
    }

    public long size() {
        return cache.size();
    }

    public void invalidateAll() {
        cache.invalidateAll();
    }

    public ImmutableMap<ExecutorCacheKey, Object> getAllPresent(@NotNull Iterable<?> keys) {
        return cache.getAllPresent(keys);
    }
}
