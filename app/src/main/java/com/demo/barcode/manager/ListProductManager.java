package com.demo.barcode.manager;

import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;

import java.util.ArrayList;
import java.util.List;

public class ListProductManager {
    private List<ProductEntity> listProduct = new ArrayList<>();
    private static ListProductManager instance;

    public static ListProductManager getInstance() {
        if (instance == null) {
            instance = new ListProductManager();
        }
        return instance;
    }

    public List<ProductEntity> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<ProductEntity> listProduct) {
        this.listProduct = listProduct;
    }

    public  ProductEntity getProductById(long productId) {
        for (ProductEntity requestEntity : listProduct) {
            if (requestEntity.getProductDetailID() == productId) {
                return requestEntity;
            }
        }
        return null;
    }

    public  ProductEntity getProductByBarcode(String barcode) {
        for (ProductEntity requestEntity : listProduct) {
            if (requestEntity.getBarcode().equals(barcode)) {
                return requestEntity;
            }
        }
        return null;
    }
}
