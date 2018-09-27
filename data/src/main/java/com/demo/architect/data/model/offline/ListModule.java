package com.demo.architect.data.model.offline;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ListModule extends RealmObject {
   @PrimaryKey
    private long id;

    private String module;

    private long userId;

    public ListModule() {
    }


    public RealmList<GroupCode> getGroupCodeRealmList() {
        return groupCodeRealmList;
    }

    public void setGroupCodeRealmList(RealmList<GroupCode> groupCodeRealmList) {
        this.groupCodeRealmList = groupCodeRealmList;
    }

    @SuppressWarnings("unused")
    private RealmList<GroupCode> groupCodeRealmList;

    public ListModule(long id, String module, long userId) {
        this.id = id;
        this.module = module;
        this.userId = userId;
    }

    public static ListModule create(Realm realm, String module, long userId){
        ListModule listModule = new ListModule(id(realm)+1,module,userId);
        listModule = realm.copyToRealm(listModule);
        return listModule;

    }

    public static long id(Realm realm) {
       long nextId = 0;
        Number maxValue = realm.where(ListModule.class).max("id");
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

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
