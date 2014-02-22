package com.optimaize.command4j.lang;

import org.jetbrains.annotations.NotNull;

/**
 * A named and typed object to be used as a key, together with a value which must
 * match the type.
 *
 * <p>Named: it has a name that uniquely identifies this key.<br/>
 * Typed: it knows the class of the data type that associated values must have.</p>
 *
 * @author Eike Kettner
 * @author Fabian Kessler
 */
@Immutable
public final class Key<V> {

    @NotNull
    private final String name;
    @NotNull
    private final Class<V> type;

    private Key(@NotNull String name, @NotNull Class<V> type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Creates a new key of the specified name and type.
     *
     * @see #stringKey(String)
     * @see #integerKey(String)
     * @see #booleanKey(String)
     */
    @NotNull
    public static <T> Key<T> create(@NotNull String name, @NotNull Class<T> type) {
        return new Key<>(name, type);
    }

    /**
     * Creates a new key of type String.
     */
    @NotNull
    public static Key<String> stringKey(@NotNull String name) {
        return create(name, String.class);
    }

    /**
     * Creates a new key of type Integer.
     */
    @NotNull
    public static Key<Integer> integerKey(@NotNull String name) {
        return create(name, Integer.class);
    }

    /**
     * Creates a new key of type Boolean.
     */
    @NotNull
    public static Key<Boolean> booleanKey(@NotNull String name) {
        return create(name, Boolean.class);
    }

    @NotNull
    public String name() {
        return name;
    }

    @NotNull
    public Class<V> type() {
        return type;
    }

    @Override
    public String toString() {
        return "Key[" + name + ":" + type.getSimpleName() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Key key = (Key) o;

        if (!name.equals(key.name)) return false;
        if (!type.equals(key.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

}
