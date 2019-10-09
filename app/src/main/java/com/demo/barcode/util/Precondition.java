package com.demo.barcode.util;


import androidx.annotation.Nullable;

/**
 * Created by ACER on 3/12/2017.
 */

public class Precondition {

    public static <T> T checkNotNull(@Nullable T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
}
