package com.tibetanapp.foxyneural.matrix;

import java.util.function.BiFunction;

/**
 * Created by Tsvetan Ovedenski on 08/08/18.
 */
class Pair <T1, T2> {
    private final T1 t1;
    private final T2 t2;

    public Pair(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public T1 fst() {
        return t1;
    }

    public T2 snd() {
        return t2;
    }

    public <R> R squash(BiFunction<T1, T2, R> f) {
        return f.apply(t1, t2);
    }

    public Pair<T2, T1> swap() {
        return new Pair<>(t2, t1);
    }

    @Override
    public String toString() {
        return "(" + t1 + "," + t2 + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }

        final Pair other = (Pair) o;
        return t1.equals(other.t1) && t2.equals(other.t2);
    }
}
