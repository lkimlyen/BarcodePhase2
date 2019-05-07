package com.demo.barcode.manager;

import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductWarehouseEntity;

import java.util.ArrayList;
import java.util.List;

public class ListProductWarehouseManager {
    private List<ProductWarehouseEntity> listProduct = new ArrayList<>();
    private static ListProductWarehouseManager instance;

    public static ListProductWarehouseManager getInstance() {
        if (instance == null) {
            instance = new ListProductWarehouseManager();
        }
        return instance;
    }

    public List<ProductWarehouseEntity> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<ProductWarehouseEntity> listProduct) {
        this.listProduct = listProduct;
    }

    public  ProductWarehouseEntity getProductById(long productId) {
        for (ProductWarehouseEntity requestEntity : listProduct) {
            if (requestEntity.getProductId() == productId) {
                return requestEntity;
            }
        }
        return null;
    }

    public  ProductWarehouseEntity getProductByBarcode(String barcode) {
        for (ProductWarehouseEntity requestEntity : listProduct) {
            if (requestEntity.getProductCode().equals(barcode)) {
                return requestEntity;
            }
        }
        return null;
    }
}
