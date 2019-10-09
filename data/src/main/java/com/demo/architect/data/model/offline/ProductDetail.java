package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.NumberInput;
import com.demo.architect.data.model.ProductEntity;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductDetail extends RealmObject {
    @PrimaryKey
    private long id;
    private long orderId;
    private long productDetailId;
    private String productName;
    private String productDetailCode;
    private String barcode;
    private String module;
    private int times;
    private int numberTotal;
    private int numberSuccess;
    private int numberScanned;
    private int numberRest;
    private long userId;
    private RealmList<Integer> listStages;

    private int status;

    public ProductDetail() {
    }

    public ProductDetail(long id, long orderId, long productDetailId, String productName, String productDetailCode, String barcode, String module, int times, int numberTotal, int numberSuccess, int numberScanned, int numberRest, long userId, int status) {
        this.id = id;
        this.orderId = orderId;
        this.productDetailId = productDetailId;
        this.productName = productName;
        this.productDetailCode = productDetailCode;
        this.barcode = barcode;
        this.module = module;
        this.times = times;
        this.numberTotal = numberTotal;
        this.numberSuccess = numberSuccess;
        this.numberScanned = numberScanned;
        this.numberRest = numberRest;
        this.userId = userId;
        this.status = status;
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

    public void setProductDetailId(long productDetailId) {
        this.productDetailId = productDetailId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductDetailCode(String productDetailCode) {
        this.productDetailCode = productDetailCode;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getProductDetailId() {
        return productDetailId;
    }

    public static ProductDetail create(Realm realm, ProductEntity productEntity, int times, long userId) {
        realm.beginTransaction();
        ProductDetail productDetail = null;
        for (NumberInput numberInput : productEntity.getListInput()) {
            if (numberInput.getTimesInput() == times) {
                productDetail = new ProductDetail(id(realm) + 1, productEntity.getOrderId(), productEntity.getProductDetailID(),
                        productEntity.getProductDetailName(), productEntity.getProductDetailCode(), productEntity.getBarcode(), productEntity.getModule(), numberInput.getTimesInput(), numberInput.getNumberTotalInput(),
                        numberInput.getNumberSuccess(), numberInput.getNumberSuccess(), numberInput.getNumberWaitting(), userId, Constants.WAITING_UPLOAD);
                productDetail = realm.copyToRealm(productDetail);
                RealmList<Integer> listStages = productDetail.getListStages();
                for (Integer id : productEntity.getListDepartmentID()) {
                    listStages.add(id);
                }
                break;
            }

        }

        realm.commitTransaction();
        return productDetail;
    }

    public static ProductDetail getProductDetail(Realm realm, ProductEntity productEntity, int times, long userId) {
        ProductDetail productDetail = realm.where(ProductDetail.class).equalTo("productDetailId", productEntity.getProductDetailID())
                .equalTo("times", times).equalTo("status",Constants.WAITING_UPLOAD)
                .equalTo("userId", userId).findFirst();

        return productDetail;
    }

    public static void updateProductDetail(Realm realm, List<ProductEntity> list, long userId) {
//        for (ProductEntity productEntity : list) {
//            ProductDetail productDetail = realm.where(ProductDetail.class).equalTo("productDetailId", productEntity.getProductDetailID())
//                    .equalTo("userId", userId).findFirst();
//            if (productDetail != null) {
//                RealmList<NumberInputModel> listNumberInput = productDetail.getListInput();
//                for (NumberInput numberInput : productEntity.getListInput()) {
//                    NumberInputModel numberInputModel = listNumberInput.where().equalTo("times", numberInput.getTimesInput()).findFirst();
//                    if (numberInputModel == null) {
//                        listNumberInput.add(NumberInputModel.create(realm, numberInput));
//                    } else {
//                        int numberTotal = numberInput.getNumberTotalInput() - numberInputModel.getNumberTotal();
//                        numberInputModel.setNumberRest(numberInputModel.getNumberRest() + numberTotal);
//                        numberInputModel.setNumberTotal(numberInput.getNumberTotalInput());
//                        numberInputModel.setNumberSuccess(numberInput.getNumberSuccess());
//                        numberInputModel.setNumberScanned(numberInputModel.getNumberTotal() - numberInputModel.getNumberRest());
//                    }
//
//                }
//            }
//
//        }
    }

    public long getId() {
        return id;
    }

    public int getTimes() {
        return times;
    }

    public int getNumberTotal() {
        return numberTotal;
    }

    public int getNumberSuccess() {
        return numberSuccess;
    }

    public int getNumberScanned() {
        return numberScanned;
    }

    public int getNumberRest() {
        return numberRest;
    }

    public long getUserId() {
        return userId;
    }

    public long getOrderId() {
        return orderId;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getModule() {
        return module;
    }

    public void setNumberSuccess(int numberSuccess) {
        this.numberSuccess = numberSuccess;
    }

    public void setNumberScanned(int numberScanned) {
        this.numberScanned = numberScanned;
    }

    public void setNumberRest(int numberRest) {
        this.numberRest = numberRest;
    }


}
