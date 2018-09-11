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
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class GroupCode extends RealmObject {

    @PrimaryKey
    private int id;

    private String groupCode;

    @SerializedName("pOrderID")
    @Expose
    private int orderId;

    private String module;
    @SerializedName("pProductDetailID")
    @Expose
    private int productDetailId;

    private String productDetailName;


    public int getNumberTotal() {
        return numberTotal;
    }

    public void setNumberTotal(int numberTotal) {
        this.numberTotal = numberTotal;
    }

    private int numberTotal;

    @SerializedName("pNumber")
    @Expose
    private int number;

    @SerializedName("pUserID")
    @Expose
    private int userId;

    private String dateGroup;
    private int status;

    public GroupCode() {
    }

    public GroupCode(int id, String groupCode, int orderId, String module, int productDetailId, String productDetailName, int numberTotal, int number, int userId, String dateGroup, int status) {
        this.id = id;
        this.groupCode = groupCode;
        this.orderId = orderId;
        this.module = module;
        this.productDetailId = productDetailId;
        this.productDetailName = productDetailName;
        this.numberTotal = numberTotal;
        this.number = number;
        this.userId = userId;
        this.dateGroup = dateGroup;
        this.status = status;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(GroupCode.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static void create(Realm realm, ProductEntity productEntity, int userId) {
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", productEntity.getOrderId()).findFirst();

        ListModule module = mainParent.getListModule().where().equalTo("module", productEntity.getModule())
                .equalTo("userId", userId).findFirst();
        RealmList<GroupCode> parentList = module.getGroupCodeRealmList();

        GroupCode groupCode = new GroupCode(id(realm) + 1, "", productEntity.getOrderId(), productEntity.getModule(),
                productEntity.getProductDetailID(), productEntity.getProductDetailName(), productEntity.getNumberTotalOrder(), 1, userId,
                DateUtils.getDateTimeCurrent(), Constants.WAITING_UPLOAD);
        groupCode = realm.copyToRealm(groupCode);
        parentList.add(groupCode);
    }


    public String getProductDetailName() {
        return productDetailName;
    }

    public void setProductDetailName(String productDetailName) {
        this.productDetailName = productDetailName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(int productDetailId) {
        this.productDetailId = productDetailId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDateGroup() {
        return dateGroup;
    }

    public void setDateGroup(String dateGroup) {
        this.dateGroup = dateGroup;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static boolean checkProductExistInGroupCode(Realm realm, ProductEntity productEntity, int userId) {
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", productEntity.getOrderId()).findFirst();

        ListModule module = mainParent.getListModule().where().equalTo("module", productEntity.getModule())
                .equalTo("userId", userId).findFirst();
        RealmList<GroupCode> parentList = module.getGroupCodeRealmList();
        GroupCode groupCode = parentList.where().equalTo("productDetailId", productEntity.getProductDetailID()).findFirst();
        return groupCode != null;
    }

    public static RealmResults<GroupCode> getListGroupCodeByModule(Realm realm, int orderId, int userId, String module) {
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();

        if (mainParent == null) {
            realm.beginTransaction();
            mainParent = new LogListScanStagesMain(orderId);
            mainParent = realm.copyToRealm(mainParent);
            realm.commitTransaction();

        }
        RealmList<ListModule> listModules = mainParent.getListModule();
        ListModule listModule = listModules.where().equalTo("module", module)
                .equalTo("userId", userId).findFirst();
        if (listModule == null) {
            realm.beginTransaction();
            listModule = ListModule.create(realm, module, userId);
            listModules.add(listModule);
            realm.commitTransaction();

        }
        RealmResults<GroupCode> parentList = listModule.getGroupCodeRealmList().where().equalTo("userId", userId)
                .equalTo("status", Constants.WAITING_UPLOAD).findAll();
        return parentList;
    }

    public static boolean checkNumberProductInGroupCode(Realm realm, ProductEntity productEntity, int userId) {
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", productEntity.getOrderId()).findFirst();

        ListModule module = mainParent.getListModule().where().equalTo("module", productEntity.getModule())
                .equalTo("userId", userId).findFirst();
        RealmResults<ListGroupCode> parentList = module.getListGroupCodes().where().equalTo("status", Constants.WAITING_UPLOAD).findAll();

        int rest = 0;
        if (parentList.size() > 0) {
            for (ListGroupCode listGroupCode : parentList) {
                GroupCode groupCode = listGroupCode.getList().where().equalTo("productDetailId", productEntity.getProductDetailID()).findFirst();
                if (groupCode != null) {
                    rest += (groupCode.getNumberTotal() - groupCode.getNumber());
                }

            }
        }

        return rest > 0;
    }

    public static boolean updateNumberGroup(Realm realm, ProductEntity productEntity, int groupId, int numberGroup, int userId) {

        GroupCode groupCode = realm.where(GroupCode.class).equalTo("id", groupId).findFirst();
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", productEntity.getOrderId()).findFirst();

        ListModule module = mainParent.getListModule().where().equalTo("module", productEntity.getModule())
                .equalTo("userId", userId).findFirst();
        RealmResults<ListGroupCode> parentList = module.getListGroupCodes().where().equalTo("status", Constants.WAITING_UPLOAD).findAll();

        int rest = 0;
        boolean exist = false;
        if (parentList.size() > 0) {
            for (ListGroupCode listGroupCode : parentList) {
                GroupCode gv = listGroupCode.getList().where().equalTo("productDetailId", productEntity.getProductDetailID()).findFirst();
                if (gv != null) {
                    rest += (gv.getNumberTotal() - gv.getNumber());
                    exist = true;
                }

            }

        }
        if (!exist) {
            rest = groupCode.getNumberTotal() - groupCode.getNumber();
        }

        if (numberGroup - groupCode.getNumber() <= rest) {
            realm.beginTransaction();
            groupCode.setNumber(numberGroup);
            realm.commitTransaction();
        }

        return numberGroup - groupCode.getNumber() <= rest;

    }

    public static void addGroupCode(Realm realm, String groupCode, int orderId, String module, GroupCode[] listSelect, int userId) {
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();

        ListModule listModule = mainParent.getListModule().where().equalTo("module", module)
                .equalTo("userId", userId).findFirst();
        RealmList<GroupCode> groupCodeRealmList = listModule.getGroupCodeRealmList();
        RealmList<ListGroupCode> listGroupCodes = listModule.getListGroupCodes();
        ListGroupCode listGroupCode = new ListGroupCode(ListGroupCode.id(realm) + 1, groupCode, module, 1, Constants.WAITING_UPLOAD);
        listGroupCode = realm.copyToRealm(listGroupCode);
        listGroupCodes.add(listGroupCode);
        RealmList<GroupCode> parentList = listGroupCode.getList();
        for (GroupCode item : listSelect) {
            GroupCode gv = realm.where(GroupCode.class).equalTo("id", item.getId()).findFirst();
            gv.setGroupCode(groupCode);
            groupCodeRealmList.remove(gv);
            parentList.add(gv);

        }


    }

    public static RealmResults<ListGroupCode> getListGroupCode(Realm realm, int orderId, String module, int userId) {
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();

        ListModule listModule = mainParent.getListModule().where().equalTo("module", module)
                .equalTo("userId", userId).findFirst();
        RealmResults<ListGroupCode> results = listModule.getListGroupCodes().where().equalTo("status", Constants.WAITING_UPLOAD).findAll();
        return results;

    }

    public static void updateGroupCode(Realm realm, String groupCode, int orderId, String module, GroupCode[] listSelect, int userId) {
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();

        ListModule listModule = mainParent.getListModule().where().equalTo("module", module)
                .equalTo("userId", userId).findFirst();
        RealmList<GroupCode> groupCodeRealmList = listModule.getGroupCodeRealmList();
        ListGroupCode listGroupCodes = listModule.getListGroupCodes().where().equalTo("groupCode", groupCode).equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        RealmList<GroupCode> inGroupCode = listGroupCodes.getList();
        for (GroupCode item : listSelect) {
            GroupCode inGroup = inGroupCode.where().equalTo("productDetailId", item.getProductDetailId()).findFirst();
            GroupCode outGroup = groupCodeRealmList.where().equalTo("productDetailId", item.getProductDetailId()).findFirst();

            if (inGroup != null) {
                inGroup.setNumber(inGroup.getNumber() + item.getNumber());
                groupCodeRealmList.remove(outGroup);
            } else {
                outGroup.setGroupCode(groupCode);
                inGroupCode.add(outGroup);
                groupCodeRealmList.remove(outGroup);

            }
        }
    }

    public static void detachedCode(Realm realm, int orderId, String module, String groupCode, int userId) {
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();

        ListModule listModule = mainParent.getListModule().where().equalTo("module", module)
                .equalTo("userId", userId).findFirst();
        RealmList<ListGroupCode> listGroupCodes = listModule.getListGroupCodes();
        ListGroupCode groupCodeList = listGroupCodes.where().equalTo("groupCode", groupCode)
                .findFirst();
        if (groupCodeList != null) {
            RealmList<GroupCode> inGroupList = groupCodeList.getList();
            RealmList<GroupCode> outGroupList = listModule.getGroupCodeRealmList();
            List<Integer> idRemoveList = new ArrayList<>();
            for (GroupCode log : groupCodeList.getList()) {
                GroupCode inGroup = inGroupList.where().equalTo("productDetailId", log.getProductDetailId()).findFirst();
                GroupCode outGroup = outGroupList.where().equalTo("productDetailId", log.getProductDetailId()).equalTo("status", Constants.WAITING_UPLOAD).findFirst();

                if (outGroup == null) {
                    inGroup.setGroupCode("");
                    outGroupList.add(inGroup);

                } else {
                    outGroup.setNumber(log.getNumber() + outGroup.getNumber());
                }
                idRemoveList.add(inGroup.id);
            }

            if (idRemoveList.size() > 0) {
                for (Integer id : idRemoveList) {
                    GroupCode logScanStages = inGroupList.where().equalTo("id", id).findFirst();
                    inGroupList.remove(logScanStages);
                }
            }
            inGroupList.deleteAllFromRealm();
            groupCodeList.deleteFromRealm();

        }
    }

    public static void removeItemInGroup(Realm realm, String listGroupCode, GroupCode groupCode, int orderId, String module, int userId) {
        LogListScanStagesMain mainParent = realm.where(LogListScanStagesMain.class).equalTo("orderId", orderId).findFirst();

        ListModule listModule = mainParent.getListModule().where().equalTo("module", module)
                .equalTo("userId", userId).findFirst();

        ListGroupCode listGroupCodes = listModule.getListGroupCodes().where().equalTo("groupCode", listGroupCode).equalTo("status", Constants.WAITING_UPLOAD).findFirst();
        RealmList<GroupCode> inGroupList = listGroupCodes.getList();

        RealmList<GroupCode> outGroupList = listModule.getGroupCodeRealmList();
        GroupCode inGroup = inGroupList.where().equalTo("productDetailId", groupCode.getProductDetailId()).findFirst();
        GroupCode outGroup = outGroupList.where().equalTo("productDetailId", groupCode.getProductDetailId()).equalTo("status", Constants.WAITING_UPLOAD).findFirst();

        if (outGroup == null) {
            inGroup.setGroupCode("");
            outGroupList.add(inGroup);
        } else {
            outGroup.setNumber(outGroup.getNumber() + inGroup.getNumber());
        }
        inGroupList.remove(inGroup);
    }


    public static List<GroupCode> updateNumberGroup(Realm realm, int id, int number) {
        ListGroupCode listGroupCode = realm.where(ListGroupCode.class).equalTo("id", id).findFirst();

        RealmList<GroupCode> list = listGroupCode.getList();
        boolean bigger = false;

        for (GroupCode groupCode : list) {
            int numberFirst = groupCode.getNumber() / listGroupCode.getNumber();
            if (numberFirst * number > groupCode.getNumberTotal()) {
                bigger = true;
                break;
            }
        }

        if (!bigger) {

            for (GroupCode groupCode : list) {
                int numberFirst = groupCode.getNumber() / listGroupCode.getNumber();
                groupCode.setNumber(numberFirst * number);

            }
            listGroupCode.setNumber(number);
        }
        return !bigger ? realm.copyFromRealm(list) : new ArrayList<GroupCode>();
    }
}
