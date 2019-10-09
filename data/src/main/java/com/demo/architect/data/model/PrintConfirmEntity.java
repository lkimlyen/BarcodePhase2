package com.demo.architect.data.model;

public class PrintConfirmEntity {

        private long  ProductID;
        private long ProductDetailID;
        private int  NumberOut;
        private int NumberDaXacNhan;
        private int NumberCF;



    public PrintConfirmEntity(long productID, long productDetailID, int numberOut, int numberDaXacNhan, int numberCF) {
        ProductID = productID;
        ProductDetailID = productDetailID;
        NumberOut = numberOut;
        NumberDaXacNhan = numberDaXacNhan;
        NumberCF = numberCF;
    }
}
