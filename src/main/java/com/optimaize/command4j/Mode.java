package com.optimaize.command4j;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.optimaize.command4j.lang.Immutable;
import com.optimaize.command4j.lang.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * The mode contains definitions that apply to a whole execution of
 * a command. They may control the behaviour of the executor by defining
 * values that can be accessed by all commands and
 * {@link ModeExtension extensions} in a chain of execution.
 *
 * <p>A Mode may be reused for multiple command executions, or it may be created
 * completely new for each call, or an existing one may serve as a base where
 * only little change is applied for a new command execution.</p>
 *
 * <p>The object is immutable; all "modification" methods return a new instance
 * and leave the old one untouched.</p>
 *
 * @author Eike Kettner
 * @author Fabian Kessler
 */
@Immutable
public final class Mode {

    /**
     * Because it's immutable anyway it does not need to be created over and over again.
     */
    private static final Mode INITIAL_MODE = new Mode();

    /**
     * Does not contain null values.
     */
    @NotNull
    private final Map<Key<?>, Object> defs = Maps.newHashMap();

    private Mode() {
    }

    private Mode(@NotNull Map<Key<?>, Object> defs) {
        this.defs.putAll(defs);
    }

    @NotNull
    public static Mode create() {
        return INITIAL_MODE;
    }

    /**
     * @return The Optional's value is <code>absent</code> only if there was no such key because the mode
     *         class does not allow <code>null</code> values.
     */
    @NotNull
    public <V> Optional<V> get(@NotNull Key<V> key) {
        //noinspection unchecked
        return (Optional<V>) Optional.fromNullable(defs.get(key));
    }

    /**
     * Returns {@code true} if the given key exists and does not map to a boolean value of {@code false}.
     *
     * <p>So any other object, such as a string for example (even if empty) or an Integer
     * (even with value 0) will return true.
     *
     * <p>Remember that this object contains no <code>null</code> values, see {@link #with}, thus
     * after "adding" <code>null</code> this <code>'is'</code> check returns false.</p>
     */
    public boolean is(@NotNull Key<?> key) {
        final Optional<?> v = get(key);
        return v.isPresent() && !v.get().equals(Boolean.FALSE);
    }

    /**
     * Creates and returns a new mode object of the current list of options together with
     * the given one.
     *
     * <p>If {@code value} is {@code null} the option key is removed (same as
     * calling {@link #without(Key)}.</p>
     *
     * <p>If there is a value for that <code>key</code> already then this overrides
     * the previously used value.</p>
     */
    @NotNull
    public <T> Mode with(@NotNull Key<T> key, @Nullable T value) {
        return new Mode(Map(this.defs, key, value));
    }

    /**
     * Short for {@link #with(Key, Object) with(Key, true)}
     */
    @NotNull
    public Mode with(@NotNull Key<Boolean> key) {
        return new Mode(Map(this.defs, key, true));
    }

    /**
     * Creates and returns a new mode object of the current list of options but without
     * the value for the given <code>key</code>.
     *
     * <p>It causes no exception in case it wasn't there.</p>
     */
    @NotNull
    public Mode without(@NotNull Key<?> key) {
        return new Mode(Map(this.defs, key, null));
    }


    @NotNull
    private static <T> Map<Key<?>, Object> Map(@NotNull Key<T> key, T value) {
        return Map(Maps.<Key<?>, Object>newHashMap(), key, value);
    }

    @NotNull
    private static <T> Map<Key<?>, Object> Map(@NotNull Map<Key<?>, Object> values, @NotNull Key<T> key1, @Nullable T value1) {
        Map<Key<?>, Object> defs = Maps.newHashMap();
        defs.putAll(values);
        if (value1 == null) {
            defs.remove(key1);
        } else {
            if (!key1.type().isAssignableFrom(value1.getClass())) {
                throw new IllegalArgumentException("Value '" + value1 + "' not appropriate for key '" + key1 + "'");
            }
            defs.put(key1, value1);
        }
        return defs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mode mode = (Mode) o;

        if (!defs.equals(mode.defs)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return defs.hashCode();
    }

    @Override
    public String toString() {
        return "Mode{" + defs + '}';
    }
}
