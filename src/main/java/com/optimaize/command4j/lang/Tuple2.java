package com.optimaize.command4j.lang;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * A simple immutable object containing two typed values.
 *
 * @author Eike Kettner
 * @author Fabian Kessler
 */
public final class Tuple2<A, B> implements Map.Entry<A, B> {

    @NotNull
    public final A _1;
    @NotNull
    public final B _2;

    public Tuple2(@NotNull A _1, @NotNull B _2) {
        this._1 = _1;
        this._2 = _2;
    }

    @Override
    public A getKey() {
        return _1;
    }

    @Override
    public B getValue() {
        return _2;
    }

    @Override
    public B setValue(B value) {
        throw new UnsupportedOperationException("Entry is immutable.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple2 tuple2 = (Tuple2) o;

        if (!_1.equals(tuple2._1)) return false;
        if (!_2.equals(tuple2._2)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _1.hashCode();
        result = 31 * result + _2.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ")";
    }
}
