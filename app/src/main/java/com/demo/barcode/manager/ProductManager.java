package com.demo.barcode.manager;

import com.demo.architect.data.model.offline.ProductModel;

public class ProductManager {
    private ProductModel productModel;
    private static ProductManager instance;

    public static ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    public ProductModel getProductModel() {
        return productModel;
    }

}
