package com.demo.barcode.manager;

import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.ProductPackagingEntity;

import java.util.ArrayList;
import java.util.List;

public class ListProductGroupManager {
    private List<ProductGroupEntity> listProduct = new ArrayList<>();
    private static ListProductGroupManager instance;

    public static ListProductGroupManager getInstance() {
        if (instance == null) {
            instance = new ListProductGroupManager();
        }
        return instance;
    }

    public List<ProductGroupEntity> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<ProductGroupEntity> listProduct) {
        this.listProduct = listProduct;
    }

    public ProductGroupEntity getProdctByBarcode(int productId) {
        for (ProductGroupEntity requestEntity : listProduct) {
            if (requestEntity.getProductDetailID() == productId) {
                return requestEntity;
            }
        }
        return null;
    }
}
