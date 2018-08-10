package com.tibetanapp.foxyneural.matrix;

import org.junit.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by Tsvetan Ovedenski on 08/08/2018.
 */
@SuppressWarnings("UnnecessaryLocalVariable")
public class MatrixTest {

    @Test(expected = IllegalArgumentException.class)
    public void testEyeNegativeSize_throws() {
        Matrix.eye(-1);
    }

    @Test
    public void testOnes() {
        final BigDecimal[][] result = Matrix.ones(2, 3).toArray();
        final BigDecimal[][] expected = new BigDecimal[][] {{ONE, ONE, ONE}, {ONE, ONE, ONE}};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testReplicate() {
        final BigDecimal[][] result = Matrix.replicate(3, 2, TEN).toArray();
        final BigDecimal[][] expected = new BigDecimal[][] {{TEN, TEN}, {TEN, TEN}, {TEN, TEN}};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testEye1() {
        final BigDecimal result = Matrix.eye(1).toScalar();
        final BigDecimal expected = ONE;
        assertEquals(expected, result);
    }
    @Test
    public void testEye2() {
        final BigDecimal[][] result = Matrix.eye(2).toArray();
        final BigDecimal[][] expected = {{ONE, ZERO}, {ZERO, ONE}};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testMult_number() {
        final Integer[][] a = new Integer[][]{
                {1, 2, 3},
                {4, 5, 6}};

        final Integer b = 10;

        final Integer[][] expected = new Integer[][] {
                {10, 20, 30},
                {40, 50, 60}};

        assertMatrixTimesNumber(a, b, expected);
    }

    @Test
    public void testMult_matrix() {
        final Integer[][] a = new Integer[][] {
                {1, 2, 3},
                {4, 5, 6}};

        final Integer[][] b = new Integer[][] {
                {1, 2},
                {3, 4},
                {5, 6}};

        final Integer[][] expected = new Integer[][] {
                {22, 28},
                {49, 64}};

        assertMatrixTimesMatrix(a, b, expected);
    }

    private void assertMatrixTimesNumber(Integer[][] a, Integer b, Integer[][] c) {
        final Matrix m1 = Matrix.of(createArray(a));

        final BigDecimal[][] result = m1.multiply(BigDecimal.valueOf(b)).toArray();
        final BigDecimal[][] expected = Matrix.of(createArray(c)).toArray();

        assertArrayEquals(expected, result);
    }

    private void assertMatrixTimesMatrix(Integer[][] a, Integer[][] b, Integer[][] c) {
        final Matrix m1 = Matrix.of(createArray(a));
        final Matrix m2 = Matrix.of(createArray(b));

        final BigDecimal[][] result = m1.multiply(m2).toArray();
        final BigDecimal[][] expected = Matrix.of(createArray(c)).toArray();

        assertArrayEquals(expected, result);
    }

    private Array2D<Integer> createArray(Integer[][] array) {
        return Array2D.of(array);
    }
}
