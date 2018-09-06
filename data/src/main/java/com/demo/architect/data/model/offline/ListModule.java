package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ListModule extends RealmObject {
    @PrimaryKey
    private int id;

    private String module;

    private int userId;

    public ListModule() {
    }




    @SuppressWarnings("unused")
    private RealmList<ListGroupCode> listGroupCodes;

    public RealmList<GroupCode> getGroupCodeRealmList() {
        return groupCodeRealmList;
    }

    public void setGroupCodeRealmList(RealmList<GroupCode> groupCodeRealmList) {
        this.groupCodeRealmList = groupCodeRealmList;
    }

    @SuppressWarnings("unused")
    private RealmList<GroupCode> groupCodeRealmList;

    public RealmList<ListGroupCode> getListGroupCodes() {
        return listGroupCodes;
    }

    public void setListGroupCodes(RealmList<ListGroupCode> listGroupCodes) {
        this.listGroupCodes = listGroupCodes;
    }

    public ListModule(int id, String module, int userId) {
        this.id = id;
        this.module = module;
        this.userId = userId;
    }

    public static ListModule create(Realm realm, String module, int userId){
        ListModule listModule = new ListModule(id(realm)+1,module,userId);
        listModule = realm.copyToRealm(listModule);
        return listModule;

    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(ListModule.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
