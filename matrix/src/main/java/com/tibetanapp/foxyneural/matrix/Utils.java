package com.tibetanapp.foxyneural.matrix;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by Tsvetan Ovedenski on 08/08/2018.
 */
public class Utils {
    static <T> void map(T[][] array, Function<T, T> f) {
        int rows = array.length;

        if (rows == 0) {
            return;
        }

        int cols = array[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                array[i][j] = f.apply(array[i][j]);
            }
        }
    }
}

class Pair <T1, T2> {
    private final T1 t1;
    private final T2 t2;

    public Pair(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public T1 first() {
        return t1;
    }

    public T2 second() {
        return t2;
    }

    public <R> R squash(BiFunction<T1, T2, R> f) {
        return f.apply(t1, t2);
    }

    @Override
    public String toString() {
        return "(" + t1 + "," + t2 + ")";
    }
}