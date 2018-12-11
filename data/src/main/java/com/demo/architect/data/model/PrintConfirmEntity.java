package com.demo.architect.data.model;

import java.util.List;

public class PrintConfirmEntity {

        private long  ProductID;
        private long ProductDetailID;
        private int  NumberOut;
        private int NumberCF;



    public PrintConfirmEntity(long productID, long productDetailID, int numberOut, int numberCF) {
        ProductID = productID;
        ProductDetailID = productDetailID;
        NumberOut = numberOut;
        NumberCF = numberCF;
    }
}
