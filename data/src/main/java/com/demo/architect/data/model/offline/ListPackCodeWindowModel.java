package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ListPackCodeWindowModel extends RealmObject {
    @PrimaryKey
    private long id;
    private long orderId;
    private long productSetId;
    private String packCode;
    private int direction;
    private int numberSetOnPack;
    private int totalNumber;
    private long serverId;
    private long userId;
    private int status;
    @SuppressWarnings("unused")
    private RealmList<LogScanPackWindowModel> list;

    public ListPackCodeWindowModel() {
    }

    public ListPackCodeWindowModel(long id, long orderId, long productSetId, String packCode, int direction, int numberSetOnPack, int totalNumber, long serverId, long userId, int status) {
        this.id = id;
        this.orderId = orderId;
        this.productSetId = productSetId;
        this.packCode = packCode;
        this.direction = direction;
        this.numberSetOnPack = numberSetOnPack;
        this.totalNumber = totalNumber;
        this.serverId = serverId;
        this.userId = userId;
        this.status = status;
    }



    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(ListPackCodeWindowModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static ListPackCodeWindowModel getListDetailPackageById(Realm realm, long id) {
        ListPackCodeWindowModel log = realm.where(ListPackCodeWindowModel.class)
                .equalTo("id", id)
                .findFirst();
        return log;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProductSetId() {
        return productSetId;
    }

    public void setProductSetId(long productSetId) {
        this.productSetId = productSetId;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getNumberSetOnPack() {
        return numberSetOnPack;
    }

    public void setNumberSetOnPack(int numberSetOnPack) {
        this.numberSetOnPack = numberSetOnPack;
    }



    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public RealmList<LogScanPackWindowModel> getList() {
        return list;
    }

    public void setList(RealmList<LogScanPackWindowModel> list) {
        this.list = list;
    }

    public int getTotalNumber() {
        return totalNumber;
    }
}
