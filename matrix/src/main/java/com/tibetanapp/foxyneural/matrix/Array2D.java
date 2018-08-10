package com.tibetanapp.foxyneural.matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Tsvetan Ovedenski on 09/08/18.
 */
public class Array2D <T> {
    private Pair<Integer, Integer> size;
    private List<List<T>> array;

    private Array2D(List<List<T>> array) {
        this.array = array;

        final int rows = array.size();
        final int cols = rows > 0 ? array.get(0).size() : 0;
        this.size = new Pair<>(rows, cols);

        assertSize(this.array, this.size);
    }

    public Pair<Integer, Integer> size() {
        return size;
    }

    public boolean isEmpty() {
        return size.fst() == 0 || size.snd() == 0;
    }

    public T get(int row, int col) {
        return array.get(row).get(col);
    }

    public void set(int row, int col, T value) {
        array.get(row).set(col, value);
    }

    public List<T> row(int i) {
        return new ArrayList<>(array.get(i));
    }

    public List<T> col(int i) {
        return array.stream().map(xs -> xs.get(i)).collect(Collectors.toList());
    }

    public List<List<T>> rows() {
        return new ArrayList<>(array);
    }

    public List<List<T>> cols() {
        final List<List<T>> out = new ArrayList<>(size.snd());

        for (int col = 0; col < size.snd(); col++) {
            final List<T> buffer = new ArrayList<>(size.fst());
            for (int row = 0; row < size.fst(); row++) {
                buffer.add(array.get(row).get(col));
            }
            out.add(buffer);
        }

        return out;
    }

    public <R> Array2D<R> map(Function<T, R> f) {
        final List<List<R>> out = array.parallelStream().map(
                rows -> rows.parallelStream().map(f).collect(Collectors.toList())
        ).collect(Collectors.toList());

        return new Array2D<>(out);
    }

    public <R> List<Pair<T, R>> zip(Array2D<R> other) {
        return new ArrayList<>();
    }

//    public void forEach(BiFunction<Integer, Integer, Consumer<T>> f) {
//        for (int row = 0; row < size.first(); row++) {
//            for (int col = 0; col < size.second(); col++) {
//                f.apply(row, col).accept(array.get(row).get(col));
//            }
//        }
//    }

    public T[][] toArray(T[][] out) {
        for (int i = 0; i < size.fst(); i++) {
            final List<T> row = array.get(i);
            for (int j = 0; j < size.snd(); j++) {
                out[i][j] = row.get(j);
            }
        }
        return out;
    }

    static <T> Array2D<T> of(T[][] input) {
        final List<List<T>> out = new ArrayList<>();

        for (T[] row : input) {
            out.add(Arrays.asList(row));
        }

        return new Array2D<>(out);
    }

    private static <T> void assertSize(List<List<T>> array, Pair<Integer, Integer> size) {
        final int cols = size.snd();
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).size() != cols) {
                throw new RuntimeException("Row #" + i + " has length " + (array.get(i).size()) + " (" + cols + " expected)");
            }
        }
    }
}
