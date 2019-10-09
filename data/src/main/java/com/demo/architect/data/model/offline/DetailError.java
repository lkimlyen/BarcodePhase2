package com.demo.architect.data.model.offline;

import android.media.Image;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DetailError extends RealmObject {
    @PrimaryKey
    private long id;

    @SuppressWarnings("unused")
    private RealmList<ImageModel> list;

    public DetailError() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RealmList<ImageModel> getList() {
        return list;
    }

    public void setList(RealmList<ImageModel> list) {
        this.list = list;
    }
}
