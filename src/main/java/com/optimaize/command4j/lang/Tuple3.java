package com.optimaize.command4j.lang;

import org.jetbrains.annotations.NotNull;

/**
 * A simple immutable object containing three typed values.
 *
 * @author Eike Kettner
 * @author Fabian Kessler
 */
public final class Tuple3<A, B, C> {

    @NotNull
    public final A _1;
    @NotNull
    public final B _2;
    @NotNull
    public final C _3;

    public Tuple3(@NotNull A _1, @NotNull B _2, @NotNull C _3) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple3 tuple3 = (Tuple3) o;

        if (!_1.equals(tuple3._1)) return false;
        if (!_2.equals(tuple3._2)) return false;
        if (!_3.equals(tuple3._3)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _1.hashCode();
        result = 31 * result + _2.hashCode();
        result = 31 * result + _3.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ", " + _3 + ")";
    }
}
