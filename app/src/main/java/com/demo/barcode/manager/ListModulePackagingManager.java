package com.demo.barcode.manager;

import com.demo.architect.data.model.ListModuleEntity;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.Result;
import com.demo.architect.data.model.offline.ListModule;

import java.util.ArrayList;
import java.util.List;

public class ListModulePackagingManager {
    private List<ListModuleEntity> list = new ArrayList<>();
    private static ListModulePackagingManager instance;

    public static ListModulePackagingManager getInstance() {
        if (instance == null) {
            instance = new ListModulePackagingManager();
        }
        return instance;
    }

    public List<ListModuleEntity> getList() {
        return list;
    }

    public void setList(List<ListModuleEntity> list) {
        this.list = list;
    }

    public List<Result> getListProductByBarcode(String barcode) {
        List<Result> results = new ArrayList<>();
        for (ListModuleEntity requestEntity : list) {
            for (PackageEntity packageEntity : requestEntity.getPackageList()) {
                for (ProductPackagingEntity productPackagingEntity : packageEntity.getProductPackagingEntityList()) {
                    if (productPackagingEntity.getBarcode().equals(barcode)) {
                        results.add(new  Result(requestEntity, packageEntity, productPackagingEntity));
                    }
                }
            }
        }
        return results;
    }

    public ListModuleEntity getModuleByModule(String module) {
        for (ListModuleEntity requestEntity : list) {

            if (requestEntity.getModule().equals(module)) {
                return requestEntity;
            }
        }
        return null;
    }

    public PackageEntity getPackingBySerialPack(String module, String serialPack) {
        for (ListModuleEntity requestEntity : list) {

            if (requestEntity.getModule().equals(module)) {

                for (PackageEntity packageEntity : requestEntity.getPackageList()) {
                    if (packageEntity.getSerialPack().equals(serialPack)) {
                        return packageEntity;
                    }
                }
            }
        }
        return null;
    }
    public PackageEntity getPackingBySerialPack(long moduleId, String serialPack) {
        for (ListModuleEntity requestEntity : list) {

            if (requestEntity.getProductId() == moduleId) {
                for (PackageEntity packageEntity : requestEntity.getPackageList()) {
                    if (packageEntity.getSerialPack().equals(serialPack)) {
                        return packageEntity;
                    }
                }
            }
        }
        return null;
    }



    public ListModuleEntity getModuleById(long moduleId) {
        for (ListModuleEntity requestEntity : list) {

            if (requestEntity.getProductId() == moduleId) {
                return requestEntity;
            }
        }
        return null;
    }
}
