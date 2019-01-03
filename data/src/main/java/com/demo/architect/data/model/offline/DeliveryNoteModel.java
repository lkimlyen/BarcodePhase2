package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DeliveryNoteModel extends RealmObject {
    @PrimaryKey
    private long id;
    private long deliveryNoteId;
    private long orderId;
    private long outputId;
    private long productDetailId;
    private double numberOut;
    private double numberRest;
    private double numberUsed;
    private double numberConfirm;

    public DeliveryNoteModel() {
    }

    public DeliveryNoteModel(long id, long deliveryNoteId, long orderId, long outputId, long productDetailId, double numberOut, double numberRest, double numberUsed, double numberConfirm) {
        this.id = id;
        this.deliveryNoteId = deliveryNoteId;
        this.orderId = orderId;
        this.outputId = outputId;
        this.productDetailId = productDetailId;
        this.numberOut = numberOut;
        this.numberRest = numberRest;
        this.numberUsed = numberUsed;
        this.numberConfirm = numberConfirm;
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

    public long getDeliveryNoteId() {
        return deliveryNoteId;
    }

    public void setDeliveryNoteId(long deliveryNoteId) {
        this.deliveryNoteId = deliveryNoteId;
    }

    public double getNumberOut() {
        return numberOut;
    }

    public void setNumberOut(double numberOut) {
        this.numberOut = numberOut;
    }

    public double getNumberRest() {
        return numberRest;
    }

    public void setNumberRest(double numberRest) {
        this.numberRest = numberRest;
    }

    public double getNumberUsed() {
        return numberUsed;
    }

    public void setNumberUsed(double numberUsed) {
        this.numberUsed = numberUsed;
    }

    public double getNumberConfirm() {
        return numberConfirm;
    }

    public void setNumberConfirm(double numberConfirm) {
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
}
