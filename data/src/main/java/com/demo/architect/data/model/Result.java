package com.demo.architect.data.model;

public class Result {
    private ListModuleEntity listModuleEntity;
    private PackageEntity packageEntity;
    private ProductPackagingEntity productPackagingEntity;

    public Result() {
    }

    public Result(ListModuleEntity listModuleEntity, PackageEntity packageEntity, ProductPackagingEntity productPackagingEntity) {
        this.listModuleEntity = listModuleEntity;
        this.packageEntity = packageEntity;
        this.productPackagingEntity = productPackagingEntity;
    }

    public ListModuleEntity getListModuleEntity() {
        return listModuleEntity;
    }

    public PackageEntity getPackageEntity() {
        return packageEntity;
    }

    public ProductPackagingEntity getProductPackagingEntity() {
        return productPackagingEntity;
    }
}
