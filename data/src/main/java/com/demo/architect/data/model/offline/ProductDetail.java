package com.demo.architect.data.model.offline;

import com.demo.architect.data.model.NumberInput;
import com.demo.architect.data.model.ProductEntity;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductDetail extends RealmObject {
    @PrimaryKey
    private long id;
    private long productId;
    private String productName;
    private String productDetailCode;
    private long userId;
    @SuppressWarnings("unused")
    private RealmList<NumberInputModel> listInput;
    private RealmList<Integer> listStages;


    public ProductDetail() {
    }

    public ProductDetail(long id, long productId, String productName, String productDetailCode, long userId) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productDetailCode = productDetailCode;
        this.userId = userId;
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

    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(ProductDetail.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductDetailCode(String productDetailCode) {
        this.productDetailCode = productDetailCode;
    }

    public long getProductId() {
        return productId;
    }

    public RealmList<NumberInputModel> getListInput() {
        return listInput;
    }

    public static ProductDetail create(Realm realm, ProductEntity productEntity, long userId) {
        ProductDetail productDetail = new ProductDetail(id(realm) + 1, productEntity.getProductDetailID(),
                productEntity.getProductDetailName(), productEntity.getProductDetailCode(), userId);
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

    public static ProductDetail getProductDetail(Realm realm, ProductEntity productEntity, long userId) {
        ProductDetail productDetail = realm.where(ProductDetail.class).equalTo("productId", productEntity.getProductDetailID())
                .equalTo("userId", userId).findFirst();
        if (productDetail == null) {
            realm.beginTransaction();
            productDetail = ProductDetail.create(realm, productEntity, userId);
            realm.commitTransaction();
        } else {
            realm.beginTransaction();
            RealmList<NumberInputModel> list = productDetail.getListInput();
            for (NumberInput numberInput : productEntity.getListInput()) {

                NumberInputModel numberInputModel = list.where().equalTo("times", numberInput.getTimesInput()).findFirst();
                if (numberInputModel == null) {
                    list.add(NumberInputModel.create(realm, numberInput));
                } else {
                    numberInputModel.setNumberTotal(numberInput.getNumberTotalInput());
                    if (numberInput.getNumberWaitting() != numberInputModel.getNumberRest()) {
                        numberInputModel.setNumberScanned((numberInputModel.getNumberScanned() - numberInputModel.getNumberSuccess()) + numberInput.getNumberSuccess());
                        numberInputModel.setNumberRest(numberInputModel.getNumberTotal() - numberInputModel.getNumberScanned());
                    }

                    numberInputModel.setNumberSuccess(numberInput.getNumberSuccess());
                }

            }
            realm.commitTransaction();

        }
        return productDetail;
    }
}
