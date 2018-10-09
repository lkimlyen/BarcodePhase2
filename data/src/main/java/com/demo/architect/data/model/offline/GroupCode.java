package com.demo.architect.data.model.offline;

import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.utils.view.DateUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class GroupCode extends RealmObject {

    @PrimaryKey
    private long id;

    @SerializedName("pOrderID")
    @Expose
    private long orderId;
    @SerializedName("pProductDetailID")
    @Expose
    private long productDetailId;

    private String productDetailName;
    @SerializedName("pNumber")
    @Expose
    private double number;

    private String module;

    @SerializedName("pUserID")
    @Expose
    private long userId;

    private String dateGroup;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(long productDetailId) {
        this.productDetailId = productDetailId;
    }

    public void setNumberTotal(double numberTotal) {
        this.numberTotal = numberTotal;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDateGroup() {
        return dateGroup;
    }

    public void setDateGroup(String dateGroup) {
        this.dateGroup = dateGroup;
    }

    private String groupCode;


    public double getNumberTotal() {
        return numberTotal;
    }

    public void setNumberTotal(int numberTotal) {
        this.numberTotal = numberTotal;
    }

    private double numberTotal;



    public GroupCode() {
    }

    public GroupCode(long id, String groupCode, long orderId, long productDetailId, String productDetailName, double numberTotal, double number, String module, long userId, String dateGroup) {
        this.id = id;
        this.groupCode = groupCode;
        this.orderId = orderId;
        this.productDetailId = productDetailId;
        this.productDetailName = productDetailName;
        this.numberTotal = numberTotal;
        this.number = number;
        this.module = module;
        this.userId = userId;
        this.dateGroup = dateGroup;
    }

    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(GroupCode.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static void create(Realm realm, ProductEntity productEntity, long userId) {

        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", productEntity.getOrderId()).findFirst();

        RealmList<GroupCode> parentList = mainParent.getGroupCodeRealmList();
        GroupCode groupCode = parentList.where().equalTo("productDetailId", productEntity.getProductDetailID()).findFirst();
        if (groupCode == null) {
            groupCode = new GroupCode(id(realm) + 1, null, productEntity.getOrderId(),
                    productEntity.getProductDetailID(), productEntity.getProductDetailName(), productEntity.getNumberTotalOrder(), 1, productEntity.getModule(), userId,
                    DateUtils.getDateTimeCurrent());
            groupCode = realm.copyToRealm(groupCode);
            parentList.add(groupCode);
        } else {
            groupCode.setNumber(groupCode.getNumber() + 1);
        }
    }

    public String getProductDetailName() {
        return productDetailName;
    }

    public void setProductDetailName(String productDetailName) {
        this.productDetailName = productDetailName;
    }


    public static RealmResults<GroupCode> getListGroupCodeByModule(Realm realm, long orderId, long userId) {
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();

        if (mainParent == null) {
            realm.beginTransaction();
            mainParent = new LogListScanStagesMain(orderId);
            mainParent = realm.copyToRealm(mainParent);
            realm.commitTransaction();

        }
        RealmResults<GroupCode> parentList = mainParent.getGroupCodeRealmList().where().equalTo("userId", userId)
                .isNull("groupCode").findAll();
        return parentList;
    }

    public static boolean checkNumberProductInGroupCode(Realm realm, ProductEntity productEntity, long userId) {
        double rest = 0;
        GroupCode groupCode = realm.where(GroupCode.class).equalTo("productDetailId", productEntity.getProductDetailID())
                .equalTo("userId", userId).isNull("groupCode").findFirst();
        if (groupCode != null) {
            rest = (groupCode.getNumberTotal() - (groupCode.getNumber()));
        } else {
            rest = productEntity.getNumberTotalOrder();
        }

        return rest > 0;
    }

    public static void updateNumberGroup(Realm realm, long groupId, double numberGroup, long userId) {
        GroupCode groupCode = realm.where(GroupCode.class).equalTo("id", groupId).equalTo("userId", userId).findFirst();
        groupCode.setNumber(numberGroup);
    }

    public static void addGroupCode(Realm realm, String groupCode, long orderId, GroupCode[] listSelect, long userId) {
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();


        RealmList<GroupCode> groupCodeRealmList = mainParent.getGroupCodeRealmList();
        for (GroupCode item : listSelect) {
            GroupCode gv = realm.where(GroupCode.class).equalTo("id", item.getId()).findFirst();
            gv.setGroupCode(groupCode);
            groupCodeRealmList.remove(gv);
        }
    }

    public static void updateGroupCode(Realm realm, String groupCode, long orderId, GroupCode[] listSelect, long userId) {
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();

        RealmList<GroupCode> groupCodeRealmList = mainParent.getGroupCodeRealmList();
        for (GroupCode item : listSelect) {
            GroupCode outGroup = groupCodeRealmList.where().equalTo("productDetailId", item.getProductDetailId()).findFirst();
            outGroup.setGroupCode(groupCode);
            groupCodeRealmList.remove(outGroup);
        }
    }

    public static void detachedCode(Realm realm, List<ProductGroupEntity> list, long orderId, long userId) {
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();
        RealmList<GroupCode> listGroupCodes = mainParent.getGroupCodeRealmList();
        for (ProductGroupEntity productGroupEntity : list) {
            GroupCode groupCode = listGroupCodes.where().equalTo("productDetailId", productGroupEntity.getProductDetailID()).isNull("groupCode").findFirst();
            if (groupCode != null){
                groupCode.setNumber(groupCode.getNumber()+ productGroupEntity.getNumber());
            }else {
                groupCode = new GroupCode(id(realm) + 1, null, orderId, productGroupEntity.getProductDetailID(),
                        productGroupEntity.getProductDetailName(), productGroupEntity.getNumberTotal(),productGroupEntity.getNumber(), productGroupEntity.getModule(), userId, DateUtils.getDateTimeCurrent());
                groupCode = realm.copyToRealm(groupCode);
                listGroupCodes.add(groupCode);
            }
        }
    }

    public static void removeItemInGroup(Realm realm, ProductGroupEntity productGroupEntity, long orderId, long userId) {
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();

        RealmList<GroupCode> outGroupList = mainParent.getGroupCodeRealmList();
        GroupCode groupCode = outGroupList.where().equalTo("productDetailId", productGroupEntity.getProductDetailID()).isNull("groupCode").findFirst();
        if (groupCode != null){
           groupCode.setNumber(groupCode.getNumber()+ productGroupEntity.getNumber());
        }else {
             groupCode = new GroupCode(id(realm) + 1, null, orderId, productGroupEntity.getProductDetailID(),
                    productGroupEntity.getProductDetailName(), productGroupEntity.getNumberTotal(),productGroupEntity.getNumber(), productGroupEntity.getModule(), userId, DateUtils.getDateTimeCurrent());
            groupCode = realm.copyToRealm(groupCode);
            outGroupList.add(groupCode);
        }


    }

    public static void deleteScanGroupCode(Realm realm, long id, long userId) {
        GroupCode groupCode = realm.where(GroupCode.class).equalTo("id", id).findFirst();
        groupCode.deleteFromRealm();
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public static Double totalNumberScanGroup(Realm realm, long productDetailId) {
        GroupCode groupCode = realm.where(GroupCode.class).equalTo("productDetailId", productDetailId).isNull("groupCode").findFirst();
        return groupCode != null ? groupCode.getNumber() : 0;
    }
}
