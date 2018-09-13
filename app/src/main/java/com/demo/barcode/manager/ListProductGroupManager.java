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

    public ProductGroupEntity getProductById(int productId) {
        for (ProductGroupEntity requestEntity : listProduct) {
            if (requestEntity.getProductDetailID() == productId) {
                return requestEntity;
            }
        }
        return null;
    }

    public List<ProductGroupEntity> getListProductByGroupCode(String groupCode) {
        List<ProductGroupEntity> list = new ArrayList<>();
        for (ProductGroupEntity requestEntity : listProduct) {
            if (requestEntity.getGroupCode().equals(groupCode)) {
                list.add(requestEntity);
            }
        }
        return list;
    }

    public List<String> getListGroupCodeByModule(String module) {
        List<String> list = new ArrayList<>();
        for (ProductGroupEntity requestEntity : listProduct) {
            if (requestEntity.getModule().equals(module)) {
                if (!list.equals(requestEntity.getGroupCode())) {
                    list.add(requestEntity.getGroupCode());
                }

            }
        }
        return list;
    }

}
