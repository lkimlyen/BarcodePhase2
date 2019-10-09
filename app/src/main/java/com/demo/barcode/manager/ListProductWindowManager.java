package com.demo.barcode.manager;

import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductWindowEntity;

import java.util.ArrayList;
import java.util.List;

public class ListProductWindowManager {
    private List<ProductWindowEntity> listProduct = new ArrayList<>();
    private static ListProductWindowManager instance;

    public static ListProductWindowManager getInstance() {
        if (instance == null) {
            instance = new ListProductWindowManager();
        }
        return instance;
    }

    public List<ProductWindowEntity> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<ProductWindowEntity> listProduct) {
        this.listProduct = listProduct;
    }

    public  ProductWindowEntity getProductById(long productId) {
        for (ProductWindowEntity requestEntity : listProduct) {
            if (requestEntity.getProductSetDetailID() == productId) {
                return requestEntity;
            }
        }
        return null;
    }

    public  ProductWindowEntity getProductByBarcode(String barcode) {
        for (ProductWindowEntity requestEntity : listProduct) {
            if (requestEntity.getBarcode().equals(barcode)) {
                return requestEntity;
            }
        }
        return null;
    }
}
