package com.demo.barcode.manager;

import com.demo.architect.data.model.ListModuleEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.ProductPackagingWindowEntity;
import com.demo.architect.data.model.Result;

import java.util.ArrayList;
import java.util.List;

public class ListProductPackagingWindowManager {
    private List<ProductPackagingWindowEntity> list = new ArrayList<>();
    private static ListProductPackagingWindowManager instance;

    public static ListProductPackagingWindowManager getInstance() {
        if (instance == null) {
            instance = new ListProductPackagingWindowManager();
        }
        return instance;
    }

    public List<ProductPackagingWindowEntity> getList() {
        return list;
    }

    public void setList(List<ProductPackagingWindowEntity> list) {
        this.list = list;
    }


    public ProductPackagingWindowEntity getDetailByBarcode(String barcode) {
        ProductPackagingWindowEntity entity = null;
        for (ProductPackagingWindowEntity item : list) {
            if (item.equals(barcode)) {
                entity = item;
                break;
            }
        }
        return entity;
    }
}
