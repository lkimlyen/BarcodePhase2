package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.utils.view.DateUtils;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ImageModel extends RealmObject {
    @PrimaryKey
    private long id;
    private long serverId;
    private String nameFile;
    private String pathFile;
    private int status;
    private String dateCreate;

    public ImageModel() {
    }

    public ImageModel(long id, String pathFile, int status, String dateCreate) {
        this.id = id;
        this.pathFile = pathFile;
        this.status = status;
        this.dateCreate = dateCreate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(ImageModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static ImageModel create(Realm realm, String pathFile) {

        ImageModel imageModel = new ImageModel(id(realm) + 1, pathFile, Constants.WAITING_UPLOAD, DateUtils.getDateTimeCurrent());
        imageModel = realm.copyToRealm(imageModel);
        return imageModel;
    }

    public static void delete(Realm realm, long id) {

        ImageModel imageModel = realm.where(ImageModel.class).equalTo("id", id).findFirst();
        if (imageModel != null) {
            imageModel.deleteFromRealm();
        }

    }


    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }


}
