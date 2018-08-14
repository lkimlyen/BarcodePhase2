package com.demo.architect.data.model.offline;

import com.demo.architect.data.model.NumberInput;
import com.demo.architect.data.model.ProductEntity;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductDetail extends RealmObject {
    @PrimaryKey
    private int productId;
    private String productName;
    private String productDetailCode;
    @SuppressWarnings("unused")
    private RealmList<NumberInputModel> listInput;
    private RealmList<Integer> listStages;


    public ProductDetail() {
    }

    public ProductDetail(int productId, String productName, String productDetailCode) {
        this.productId = productId;
        this.productName = productName;
        this.productDetailCode = productDetailCode;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDetailCode() {
        return productDetailCode;
    }

    public RealmList<Integer> getListStages() {
        return listStages;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(ProductDetail.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductDetailCode(String productDetailCode) {
        this.productDetailCode = productDetailCode;
    }

    public int getProductId() {
        return productId;
    }

    public RealmList<NumberInputModel> getListInput() {
        return listInput;
    }

    public static ProductDetail create(Realm realm, ProductEntity productEntity) {
        ProductDetail productDetail = new ProductDetail(productEntity.getProductDetailID(),
                productEntity.getProductDetailName(), productEntity.getProductDetailCode());
        productDetail = realm.copyToRealm(productDetail);
        RealmList<NumberInputModel> numberInputModels = productDetail.getListInput();
        for (NumberInput numberInput : productEntity.getListInput()) {
            numberInputModels.add(NumberInputModel.create(realm, numberInput));
        }

        RealmList<Integer> listStages = productDetail.getListStages();
        for (Integer id : productEntity.getListDepartmentID()) {
            listStages.add(id);
        }
        return productDetail;
    }

    public static ProductDetail getProductDetail(Realm realm, ProductEntity productEntity) {
        ProductDetail productDetail = realm.where(ProductDetail.class).equalTo("productId", productEntity.getProductDetailID()).findFirst();
        if (productDetail == null) {
            realm.beginTransaction();
            productDetail = ProductDetail.create(realm, productEntity);
            realm.commitTransaction();
        } else {
            if (productDetail.getListInput().size() < productEntity.getListInput().size()) {
                realm.beginTransaction();
                RealmList<NumberInputModel> list = productDetail.getListInput();
                for (NumberInput numberInput : productEntity.getListInput()) {

                    NumberInputModel numberInputModel = list.where().equalTo("times", numberInput.getTimesInput()).findFirst();
                    if (numberInputModel == null) {
                        list.add(NumberInputModel.create(realm, numberInput));
                    }

                }
                realm.commitTransaction();
            }

        }
        return productDetail;
    }
}
