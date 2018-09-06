package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.utils.view.DateUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogScanStages extends RealmObject {

    @PrimaryKey
    private int id;
    @Expose
    @SerializedName("pOrderID")
    private int orderId;
    @Expose
    @SerializedName("pDepartmentIDIn")
    private int departmentIdIn;
    @Expose
    @SerializedName("pDepartmentIDOut")
    private int departmentIdOut;
    @Expose
    @SerializedName("pProductDetailID")
    private int productDetailId;

    private ProductDetail productDetail;
    @Expose
    @SerializedName("pBarcode")
    private String barcode;

    private String module;
    @Expose
    @SerializedName("pNumberScan")
    private int numberInput;
    @Expose
    @SerializedName("pTimes")
    private int times;
    @Expose
    @SerializedName("pDateTimeScan")
    private String dateScan;
    @Expose
    @SerializedName("pUserID")
    private int userId;

    private int numberGroup;

    public LogScanStages() {
    }


    public LogScanStages(int orderId, int departmentIdIn, int departmentIdOut, int productDetailId, String barcode, String module, int numberInput, int times, String dateScan, int userId, int numberGroup) {
        this.orderId = orderId;
        this.departmentIdIn = departmentIdIn;
        this.departmentIdOut = departmentIdOut;
        this.productDetailId = productDetailId;
        this.barcode = barcode;
        this.module = module;
        this.numberInput = numberInput;
        this.times = times;
        this.dateScan = dateScan;
        this.userId = userId;
        this.numberGroup = numberGroup;
    }

    public int getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getModule() {
        return module;
    }


    public int getNumberInput() {
        return numberInput;
    }

    public int getTimes() {
        return times;
    }

    public String getDateScan() {
        return dateScan;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setNumberInput(int numberInput) {
        this.numberInput = numberInput;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public void setDateScan(String dateScan) {
        this.dateScan = dateScan;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getDepartmentIdIn() {
        return departmentIdIn;
    }

    public void setDepartmentIdIn(int departmentIdIn) {
        this.departmentIdIn = departmentIdIn;
    }

    public int getDepartmentIdOut() {
        return departmentIdOut;
    }

    public void setDepartmentIdOut(int departmentIdOut) {
        this.departmentIdOut = departmentIdOut;
    }

    public int getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(int productDetailId) {
        this.productDetailId = productDetailId;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(LogScanStages.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }


    public static void addLogScanStages(Realm realm, LogScanStages scanStages, ProductEntity productEntity) {
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", scanStages.getOrderId()).findFirst();

        LogListScanStages parent = mainParent.getList().where().equalTo("departmentId", scanStages.getDepartmentIdIn())
                .equalTo("date", DateUtils.getShortDateCurrent()).equalTo("times", scanStages.getTimes()).equalTo("userId", scanStages.getUserId()).equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        RealmList<LogScanStages> parentList = parent.getList();
        ProductDetail productDetail = realm.where(ProductDetail.class).equalTo("productId", scanStages.getProductDetailId())
                .equalTo("userId", scanStages.getUserId()).findFirst();
        if (productDetail == null) {
            productDetail = ProductDetail.create(realm, productEntity, scanStages.userId);
        }
        LogScanStages logScanStages = parent.getList().where().equalTo("barcode", scanStages.getBarcode())
                .equalTo("module", scanStages.getModule()).equalTo("times", scanStages.getTimes()).findFirst();
        if (logScanStages == null) {
            scanStages.setProductDetail(productDetail);
            scanStages.setId(id(realm) + 1);
            logScanStages = realm.copyToRealm(scanStages);
            parentList.add(logScanStages);
        } else {
            logScanStages.setNumberInput(logScanStages.getNumberInput() + scanStages.getNumberInput());
            logScanStages.setNumberGroup(logScanStages.getNumberInput());
        }
        NumberInputModel numberInputModel = productDetail.getListInput().where().equalTo("times", scanStages.getTimes()).findFirst();
        numberInputModel.setNumberScanned(numberInputModel.getNumberScanned() + scanStages.getNumberInput());
        numberInputModel.setNumberRest(numberInputModel.getNumberTotal() - numberInputModel.getNumberScanned());
    }

    public static void updateNumberInput(Realm realm, int stagesId, int numberInput) {
        LogScanStages logScanStages = realm.where(LogScanStages.class).equalTo("id", stagesId).findFirst();
        int number = numberInput - logScanStages.getNumberInput();
        logScanStages.setNumberInput(numberInput);
        logScanStages.setNumberGroup(numberInput);
        ProductDetail productDetail = logScanStages.getProductDetail();
        NumberInputModel numberInputModel = productDetail.getListInput().where().equalTo("times", logScanStages.getTimes()).findFirst();
        numberInputModel.setNumberScanned(numberInputModel.getNumberScanned() + number);
        numberInputModel.setNumberRest(numberInputModel.getNumberTotal() - numberInputModel.getNumberScanned());


    }

    public static void deleteScanStages(Realm realm, int stagesId) {
        LogScanStages logScanStages = realm.where(LogScanStages.class).equalTo("id", stagesId).findFirst();
        ProductDetail productDetail = logScanStages.getProductDetail();
        NumberInputModel numberInputModel = productDetail.getListInput().where().equalTo("times", logScanStages.getTimes()).findFirst();
        numberInputModel.setNumberScanned(numberInputModel.getNumberScanned() - logScanStages.getNumberInput());
        numberInputModel.setNumberRest(numberInputModel.getNumberTotal() - numberInputModel.getNumberScanned());
        logScanStages.deleteFromRealm();
    }


    public int getNumberGroup() {
        return numberGroup;
    }

    public void setNumberGroup(int numberGroup) {
        this.numberGroup = numberGroup;
    }



    public static void updateNumberGroup(Realm realm, int logId, int numberGroup) {
        LogScanStages logScanStages = realm.where(LogScanStages.class).equalTo("id", logId).findFirst();
        logScanStages.setNumberGroup(numberGroup);
    }

//    public static void detachedCodeStages(Realm realm, int orderId, int departmentId, int times, ListGroupCode list, int userId) {
//        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();
//
//        LogListScanStages parent = mainParent.getList().where().equalTo("departmentId", departmentId)
//                .equalTo("date", DateUtils.getShortDateCurrent()).equalTo("times", times).equalTo("userId", userId).equalTo("status", Constants.WAITING_UPLOAD).findFirst();
//        RealmList<LogScanStages> parentList = parent.getList();
//        RealmList<ListGroupCode> listGroupCodes = parent.getListGroupCodes();
//        ListGroupCode groupCodeList = listGroupCodes.where().equalTo("id", list.getId())
//                .findFirst();
//
////        RealmList<LogScanStages> listScanStagesGroup = groupCodeList.getList();
////        List<Integer> idRemoveList = new ArrayList<>();
////        for (LogScanStages log : list.getList()) {
////            LogScanStages logCheck = parentList.where().equalTo("barcode", log.barcode).findFirst();
////            LogScanStages logMain = realm.where(LogScanStages.class).equalTo("id", log.getId()).findFirst();
////            if (logCheck == null) {
////                parentList.add(logMain);
////                idRemoveList.add(logMain.id);
////            } else {
////                logCheck.setNumberInput(log.getNumberGroup() + logCheck.getNumberGroup());
////            }
////        }
////
////        if (idRemoveList.size() > 0) {
////            for (Integer id : idRemoveList) {
////                LogScanStages logScanStages = listScanStagesGroup.where().equalTo("id", id).findFirst();
////                listScanStagesGroup.remove(logScanStages);
////            }
////        }
////        listScanStagesGroup.deleteAllFromRealm();
////        groupCodeList.deleteFromRealm();
//
//
//    }

//    public static void removeItemInGroup(Realm realm, ListGroupCode groupCode, LogScanStages logScanStages, int orderId, int departmentId, int times, int userId) {
//        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();
//
//        LogListScanStages parent = mainParent.getList().where().equalTo("departmentId", departmentId)
//                .equalTo("date", DateUtils.getShortDateCurrent()).equalTo("times", times).equalTo("userId", userId).equalTo("status", Constants.WAITING_UPLOAD).findFirst();
//        RealmList<LogScanStages> parentList = parent.getList();
//        RealmList<ListGroupCode> listGroupCodes = parent.getListGroupCodes();
//        ListGroupCode groupCodeList = listGroupCodes.where().equalTo("id", groupCode.getId())
//                .findFirst();
//
////        RealmList<LogScanStages> listScanStagesGroup = groupCodeList.getList();
////
////        LogScanStages logInGroup = listScanStagesGroup.where().equalTo("id", logScanStages.getId()).findFirst();
////
////        LogScanStages logCheck = parentList.where().equalTo("barcode", logScanStages.getBarcode()).findFirst();
////        if (logCheck == null) {
////            parentList.add(logInGroup);
////        } else {
////            logCheck.setNumberInput(logCheck.getNumberInput() + logInGroup.getNumberGroup());
////            logCheck.setNumberGroup(logInGroup.getNumberInput());
////        }
////        listScanStagesGroup.remove(logInGroup);
//    }

//
}
