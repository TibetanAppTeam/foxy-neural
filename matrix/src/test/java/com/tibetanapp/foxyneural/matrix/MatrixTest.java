package com.tibetanapp.foxyneural.matrix;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;

/**
 * Created by Tsvetan Ovedenski on 08/08/2018.
 */
public class MatrixTest {

    @Test(expected = AssertionError.class)
    public void testEyeNegativeSize_throws() {
        Matrix.eye(-1);
    }

    @Test
    public void testEye1() {
        final BigDecimal arr = Matrix.eye(1).toScalar();
        final BigDecimal expected = ONE;
        Assert.assertEquals(expected, arr);
    }
    @Test
    public void testEye2() {
        final BigDecimal[][] arr = Matrix.eye(2).toArray();
        final BigDecimal[][] expected = {{ONE, ZERO}, {ZERO, ONE}};
        Assert.assertArrayEquals(expected, arr);
    }

    @Test
    public void testMult_matrix() {
        final BigDecimal[][] arr1 = {
                {v(1), v(2), v(3)},
                {v(4), v(5), v(6)}};
        final Matrix m1 = new Matrix(arr1);

        final BigDecimal[][] arr2 = {
                {v(1), v(2)},
                {v(3), v(4)},
                {v(5), v(6)}};
        final Matrix m2 = new Matrix(arr2);

        final BigDecimal[][] result = m1.multiply(m2).toArray();
        final BigDecimal[][] exp = {
                {v(22), v(28)},
                {v(49), v(64)}};

        Assert.assertArrayEquals(exp, result);
    }

    private BigDecimal v(long val) {
        return valueOf(val);
    }
}
