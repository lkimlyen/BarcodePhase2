package com.demo.barcode.manager;

import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductPackagingEntity;

import java.util.ArrayList;
import java.util.List;

public class ListProductPackagingManager {
    private List<ProductPackagingEntity> listProduct = new ArrayList<>();
    private static ListProductPackagingManager instance;

    public static ListProductPackagingManager getInstance() {
        if (instance == null) {
            instance = new ListProductPackagingManager();
        }
        return instance;
    }

    public List<ProductPackagingEntity> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<ProductPackagingEntity> listProduct) {
        this.listProduct = listProduct;
    }

        public ProductPackagingEntity getProdctByBarcode(String barcode) {
        for (ProductPackagingEntity requestEntity : listProduct) {
            if (requestEntity.getBarcode() == barcode) {
                return requestEntity;
            }
        }
        return null;
    }
}
