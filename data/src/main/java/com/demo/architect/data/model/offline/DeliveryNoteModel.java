package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DeliveryNoteModel extends RealmObject {
    @PrimaryKey
    private long id;
    private long orderId;
    private long outputId;
    private long productId;
    private String module;
    private long productDetailId;
    private String productDetailName;
    private int numberOut;
    private int numberRest;
    private int numberUsed;
    private int numberConfirm;
    private int status;

    public DeliveryNoteModel() {
    }

    public DeliveryNoteModel(long id, long orderId, long outputId, long productId, String module, long productDetailId, String productDetailName, int numberOut, int numberRest, int numberUsed, int numberConfirm, int status) {
        this.id = id;
        this.orderId = orderId;
        this.outputId = outputId;
        this.productId = productId;
        this.module = module;
        this.productDetailId = productDetailId;
        this.productDetailName = productDetailName;
        this.numberOut = numberOut;
        this.numberRest = numberRest;
        this.numberUsed = numberUsed;
        this.numberConfirm = numberConfirm;
        this.status = status;
    }

    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(DeliveryNoteModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public int getNumberOut() {
        return numberOut;
    }

    public void setNumberOut(int numberOut) {
        this.numberOut = numberOut;
    }

    public int getNumberRest() {
        return numberRest;
    }

    public void setNumberRest(int numberRest) {
        this.numberRest = numberRest;
    }

    public int getNumberUsed() {
        return numberUsed;
    }

    public void setNumberUsed(int numberUsed) {
        this.numberUsed = numberUsed;
    }

    public int getNumberConfirm() {
        return numberConfirm;
    }

    public void setNumberConfirm(int numberConfirm) {
        this.numberConfirm = numberConfirm;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getProductDetailId() {
        return productDetailId;
    }

    public long getOutputId() {
        return outputId;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getProductId() {
        return productId;
    }

    public String getModule() {
        return module;
    }

    public String getProductDetailName() {
        return productDetailName;
    }

    public int getStatus() {
        return status;
    }
}
