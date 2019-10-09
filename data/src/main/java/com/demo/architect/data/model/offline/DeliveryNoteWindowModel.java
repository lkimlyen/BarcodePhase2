package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DeliveryNoteWindowModel extends RealmObject {
    @PrimaryKey
    private long id;
    private long outputId;
    private long productSetId;
    private String productSetName;
    private long productSetDetailId;
    private String productSetDetailName;
    private int numberOut;
    private int numberRest;
    private int numberUsed;
    private int numberConfirm;
    private int status;

    public DeliveryNoteWindowModel() {
    }

    public DeliveryNoteWindowModel(long id, long outputId, long productSetId, String productSetName, long productSetDetailId, String productSetDetailName, int numberOut, int numberRest, int numberUsed, int numberConfirm, int status) {
        this.id = id;
        this.outputId = outputId;
        this.productSetId = productSetId;
        this.productSetName = productSetName;
        this.productSetDetailId = productSetDetailId;
        this.productSetDetailName = productSetDetailName;
        this.numberOut = numberOut;
        this.numberRest = numberRest;
        this.numberUsed = numberUsed;
        this.numberConfirm = numberConfirm;
        this.status = status;
    }

    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(DeliveryNoteWindowModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public long getId() {
        return id;
    }

    public long getOutputId() {
        return outputId;
    }

    public long getProductSetId() {
        return productSetId;
    }

    public String getProductSetName() {
        return productSetName;
    }

    public long getProductSetDetailId() {
        return productSetDetailId;
    }

    public String getProductSetDetailName() {
        return productSetDetailName;
    }

    public int getNumberOut() {
        return numberOut;
    }

    public int getNumberRest() {
        return numberRest;
    }

    public int getNumberUsed() {
        return numberUsed;
    }

    public int getNumberConfirm() {
        return numberConfirm;
    }

    public void setNumberRest(int numberRest) {
        this.numberRest = numberRest;
    }

    public void setNumberUsed(int numberUsed) {
        this.numberUsed = numberUsed;
    }

    public void setNumberConfirm(int numberConfirm) {
        this.numberConfirm = numberConfirm;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
