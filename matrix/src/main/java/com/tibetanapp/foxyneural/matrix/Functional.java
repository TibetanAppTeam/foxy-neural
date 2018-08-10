package com.tibetanapp.foxyneural.matrix;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Tsvetan Ovedenski on 08/08/2018.
 */
class Functional {
    static <T> void mapMutable(T[][] array, Function<T, T> f) {
        final int rows = array.length;
        if (rows == 0) {
            return;
        }

        final int cols = array[0].length;
        if (cols == 0) {
            return;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                array[i][j] = f.apply(array[i][j]);
            }
        }
    }

    // https://stackoverflow.com/a/31964093
    static <A, B> List<Pair<A, B>> zip(List<A> as, List<B> bs) {
        return IntStream.range(0, Math.min(as.size(), bs.size()))
                .mapToObj(i -> new Pair<>(as.get(i), bs.get(i)))
                .collect(Collectors.toList());
    }
}