package com.demo.architect.data.model.offline;

import com.demo.architect.data.model.NumberInput;
import com.demo.architect.data.model.ProductEntity;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class ProductDetail extends RealmObject {
    @PrimaryKey
    private long id;
    private long productId;
    private String productName;
    private String productDetailCode;
    private int times;
    private double numberTotal;
    private double numberSuccess;
    private double numberScanned;
    private double numberRest;
    private long userId;
    private RealmList<Integer> listStages;


    public ProductDetail() {
    }

    public ProductDetail(long id, long productId, String productName, String productDetailCode, int times, double numberTotal, double numberSuccess, double numberScanned, double numberRest, long userId) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productDetailCode = productDetailCode;
        this.times = times;
        this.numberTotal = numberTotal;
        this.numberSuccess = numberSuccess;
        this.numberScanned = numberScanned;
        this.numberRest = numberRest;
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

    public static void create(Realm realm, List<ProductEntity> list, long userId) {
        RealmResults<ProductDetail> results = realm.where(ProductDetail.class).findAll();
        results.deleteAllFromRealm();
        for (ProductEntity productEntity : list) {
            for (NumberInput numberInput : productEntity.getListInput()) {
                ProductDetail productDetail = new ProductDetail(id(realm) + 1, productEntity.getProductDetailID(),
                        productEntity.getProductDetailName(), productEntity.getProductDetailCode(), numberInput.getTimesInput(), numberInput.getNumberTotalInput(),
                        numberInput.getNumberSuccess(), numberInput.getNumberSuccess(), numberInput.getNumberWaitting(), userId);
                productDetail = realm.copyToRealm(productDetail);
                RealmList<Integer> listStages = productDetail.getListStages();
                for (Integer id : productEntity.getListDepartmentID()) {
                    listStages.add(id);
                }
            }
        }
    }

    public static ProductDetail getProductDetail(Realm realm, ProductEntity productEntity,int times, long userId) {
        ProductDetail productDetail = realm.where(ProductDetail.class).equalTo("productId", productEntity.getProductDetailID())
                .equalTo("times",times)
                .equalTo("userId", userId).findFirst();
        return productDetail;
    }

    public static void updateProductDetail(Realm realm, List<ProductEntity> list, long userId) {
        for (ProductEntity productEntity : list) {
            ProductDetail productDetail = realm.where(ProductDetail.class).equalTo("productId", productEntity.getProductDetailID())
                    .equalTo("userId", userId).findFirst();
            if (productDetail != null) {
                RealmList<NumberInputModel> listNumberInput = productDetail.getListInput();
                for (NumberInput numberInput : productEntity.getListInput()) {
                    NumberInputModel numberInputModel = listNumberInput.where().equalTo("times", numberInput.getTimesInput()).findFirst();
                    if (numberInputModel == null) {
                        listNumberInput.add(NumberInputModel.create(realm, numberInput));
                    } else {
                        double numberTotal = numberInput.getNumberTotalInput() - numberInputModel.getNumberTotal();
                        numberInputModel.setNumberRest(numberInputModel.getNumberRest() + numberTotal);
                        numberInputModel.setNumberTotal(numberInput.getNumberTotalInput());
                        numberInputModel.setNumberSuccess(numberInput.getNumberSuccess());
                        numberInputModel.setNumberScanned(numberInputModel.getNumberTotal() - numberInputModel.getNumberRest());
                    }

                }
            }

        }
    }

    public long getId() {
        return id;
    }

    public int getTimes() {
        return times;
    }

    public double getNumberTotal() {
        return numberTotal;
    }

    public double getNumberSuccess() {
        return numberSuccess;
    }

    public double getNumberScanned() {
        return numberScanned;
    }

    public double getNumberRest() {
        return numberRest;
    }

    public long getUserId() {
        return userId;
    }
}
