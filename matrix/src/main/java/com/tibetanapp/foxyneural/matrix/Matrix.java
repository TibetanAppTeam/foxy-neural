package com.tibetanapp.foxyneural.matrix;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tibetanapp.foxyneural.matrix.Utils.map;

/**
 * Created by Tsvetan Ovedenski on 08/08/2018.
 */
public class Matrix {
    private int rows;
    private int cols;
    private BigDecimal[][] matrix;

    public Matrix(BigDecimal[][] matrix) {
        this.rows = matrix.length;

        assert this.rows > 0;
        this.cols = matrix[0].length;
        this.matrix = matrix.clone();
    }

    public void printSize() {
        System.out.println("Size: " + rows + "X" + cols);
    }

    public Matrix multiply(BigDecimal value) {
        final Matrix result = new Matrix(this.matrix);
        map(result.matrix, (v) -> v.multiply(value));
        return result;
    }

    public Matrix multiply(Matrix other) {
        if (cols != other.rows) {
            throw new IllegalArgumentException("Sizes mismatch");
        }

        final int newRows = rows;
        final int newCols = other.cols;
        BigDecimal[][] arr = new BigDecimal[newRows][newCols];

        final List<List<Pair<BigDecimal, BigDecimal>>> lists = zip(other);
        System.out.println(lists);
        final BigDecimal[] list = lists.stream()
                .map(ls -> ls.stream()
                        .map(l -> l.squash(BigDecimal::multiply))
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .collect(Collectors.toList()).toArray(new BigDecimal[] {});
        System.out.println(list.length);

        for (int r = 0; r < newRows; r++) {
            for (int c = 0; c < newCols; c++) {
                final int i = c * newRows + r;
                arr[r][c] = list[i];
            }
        }

        return new Matrix(arr);
    }

    private List<List<Pair<BigDecimal, BigDecimal>>> zip(Matrix other) {
        int minRow = Math.min(rows, other.cols);
        int minCol = Math.max(cols, other.rows);

        final List<List<Pair<BigDecimal, BigDecimal>>> list = new ArrayList<>();
        for (int r = 0; r < minRow; r++) {
            final List<Pair<BigDecimal, BigDecimal>> buffer = new ArrayList<>();
            for (int c = 0; c < minCol; c++) {
                final Pair<BigDecimal, BigDecimal> p = new Pair<>(matrix[r][c], other.matrix[c][r]);
                buffer.add(p);
            }
            list.add(buffer);
        }
        return list;
    }

    public BigDecimal[][] toArray() {
        return matrix.clone();
    }

    public BigDecimal toScalar() {
        if (rows > 1 || cols > 1) {
            throw new RuntimeException("Scalar from matrix with many rows/cols");
        }
        return matrix[0][0];
    }

    static Matrix zeros(int rows, int cols) {
        return replicate(rows, cols, BigDecimal.ZERO);
    }

    static Matrix ones(int rows, int cols) {
        return replicate(rows, cols, BigDecimal.ONE);
    }

    static Matrix eye(int size) {
        assert size > 0;
        final Matrix matrix = zeros(size, size);
        for (int i = 0; i < size; i++) {
            matrix.matrix[i][i] = BigDecimal.ONE;
        }
        return matrix;
    }

    static Matrix replicate(int rows, int cols, BigDecimal value) {
        final BigDecimal[][] arr = new BigDecimal[rows][cols];
        map(arr, v -> BigDecimal.ZERO);
        return new Matrix(arr);
    }
}