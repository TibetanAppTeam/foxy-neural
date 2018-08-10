package com.tibetanapp.foxyneural.matrix;

/**
 * Created by Tsvetan Ovedenski on 08/08/18.
 */
public class Arguments {
    public static void checkThat(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
