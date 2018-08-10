package com.tibetanapp.foxyneural.matrix;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.tibetanapp.foxyneural.matrix.Functional.mapMutable;

/**
 * Created by Tsvetan Ovedenski on 08/08/2018.
 */
public class Matrix {
    private int rows;
    private int cols;
    private Array2D<BigDecimal> matrix;

    private Matrix(BigDecimal[][] matrix) {
        this(Array2D.of(matrix));
    }

    private Matrix(Array2D<BigDecimal> matrix) {
        this.matrix = matrix;

        this.rows = this.matrix.size().fst();
        Arguments.checkThat(this.rows > 0, "rows is less than 1");

        this.cols = this.matrix.size().snd();
        Arguments.checkThat(this.cols > 0, "cols is less than 1");
    }

    public Matrix multiply(BigDecimal value) {
        final Matrix result = of(this);
        result.matrix = result.matrix.map(v -> v.multiply(value));
        return result;
    }

    public Matrix multiply(Matrix other) {
        Arguments.checkThat(cols == other.rows, "sizes mismatch");

        final int newRows = rows;
        final int newCols = other.cols;
        BigDecimal[][] arr = new BigDecimal[newRows][newCols];

        final List<BigDecimal> list = zip(other).parallelStream()
                .map(xs -> xs.stream()
                        .map(x -> x.squash(BigDecimal::multiply))
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .collect(Collectors.toList());

        for (int r = 0; r < newRows; r++) {
            for (int c = 0; c < newCols; c++) {
                final int i = r * newRows + c;
                arr[r][c] = list.get(i);
            }
        }

        return new Matrix(arr);
    }

    private List<List<Pair<BigDecimal, BigDecimal>>> zip(Matrix other) {
        final int total = Math.min(rows, other.cols);

//        New way:
        return IntStream
                .range(0, total)
                .mapToObj(i -> new Pair<>(i, IntStream.range(0, total)))
                .flatMap(p -> p.snd().mapToObj(j -> Functional.zip(matrix.row(p.fst()), other.matrix.col(j))))
                .collect(Collectors.toList());

//        Old way:
//        final List<List<Pair<BigDecimal, BigDecimal>>> out = new ArrayList<>();
//
//        for (int i = 0; i < total; i++) {
//            final List<BigDecimal> as = matrix.row(i);
//            for (int j = 0; j < total; j++) {
//                final List<BigDecimal> bs = other.matrix.col(j);
//                out.add(Functional.zip(as, bs));
//            }
//        }
//
//        return out;
    }

    public BigDecimal[][] toArray() {
        return matrix.toArray(new BigDecimal[rows][cols]);
    }

    public BigDecimal toScalar() {
        if (rows > 1 || cols > 1) {
            throw new RuntimeException("Scalar from matrix with many rows/cols");
        }
        return matrix.get(0, 0);
    }

    static Matrix zeros(int rows, int cols) {
        return replicate(rows, cols, BigDecimal.ZERO);
    }

    static Matrix ones(int rows, int cols) {
        return replicate(rows, cols, BigDecimal.ONE);
    }

    static Matrix eye(int size) {
        Arguments.checkThat(size > 0, "size is less than 1");

        final Matrix matrix = zeros(size, size);

        for (int i = 0; i < size; i++) {
            matrix.matrix.set(i, i, BigDecimal.ONE);
        }

        return matrix;
    }

    static Matrix replicate(int rows, int cols, BigDecimal value) {
        Arguments.checkThat(rows > 0, "zero or negative rows size");
        Arguments.checkThat(cols > 0, "zero or negative cols size");

        final BigDecimal[][] arr = new BigDecimal[rows][cols];
        mapMutable(arr, v -> value);
        return new Matrix(arr);
    }

    static Matrix of(BigDecimal[][] matrix) {
        return new Matrix(matrix.clone());
    }

    static <T> Matrix of(Array2D<T> array, Function<T, BigDecimal> mapper) {
        return new Matrix(array.map(mapper));
    }

    static Matrix of(Array2D<Integer> array) {
        return of(array, BigDecimal::new);
    }

    static Matrix of(Matrix other) {
        return of(other.toArray());
    }
}