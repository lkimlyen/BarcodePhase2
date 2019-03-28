package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogListSerialPackPagkaging extends RealmObject {
    @PrimaryKey
    private long id;
    private long orderId;
    private long apartmentId;
    private String serialPack;
    private String codeProduct;
    private long productId;
    private String module;
    private int numberTotal;
    private int size;
    private int status;
    private long serverId;
    @SuppressWarnings("unused")
    private RealmList<LogScanPackaging> list;

    public LogListSerialPackPagkaging() {
    }

    public LogListSerialPackPagkaging(long id, long orderId, long apartmentId, String serialPack, String codeProduct, long productId, int numberTotal, int size, String module, int status) {
        this.id = id;
        this.orderId = orderId;
        this.apartmentId = apartmentId;
        this.serialPack = serialPack;
        this.codeProduct = codeProduct;
        this.productId = productId;
        this.numberTotal = numberTotal;
        this.size = size;
        this.module = module;
        this.status = status;
    }


    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(LogListSerialPackPagkaging.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static LogListSerialPackPagkaging create(Realm realm, long orderId, long apartmentId, String serialPack, String codeProduct, long productId, int numberTotal, String module) {
        LogListSerialPackPagkaging log = new LogListSerialPackPagkaging(id(realm) + 1, orderId, apartmentId, serialPack, codeProduct, productId, numberTotal, 0, module, Constants.WAITING_UPLOAD);
        log = realm.copyToRealm(log);
        return log;
    }

    public static LogListSerialPackPagkaging getListDetailPackageById(Realm realm, long logSerialId) {
        LogListSerialPackPagkaging log = realm.where(LogListSerialPackPagkaging.class)
                .equalTo("id", logSerialId)
                .findFirst();
        return log;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RealmList<LogScanPackaging> getList() {
        return list;
    }

    public void setList(RealmList<LogScanPackaging> list) {
        this.list = list;
    }

    public String getSerialPack() {
        return serialPack;
    }

    public void setSerialPack(String serialPack) {
        this.serialPack = serialPack;
    }

    public String getCodeProduct() {
        return codeProduct;
    }

    public void setCodeProduct(String codeProduct) {
        this.codeProduct = codeProduct;
    }

    @Override
    public String toString() {
        return serialPack;
    }

    public int getNumberTotal() {
        return numberTotal;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setNumberTotal(int numberTotal) {
        this.numberTotal = numberTotal;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getApartmentId() {
        return apartmentId;
    }

    public int getStatus() {
        return status;
    }

    public long getServerId() {
        return serverId;
    }
}
