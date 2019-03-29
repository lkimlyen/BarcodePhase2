package com.demo.barcode.manager;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.SetWindowEntity;

import java.util.List;

public class ListSetWindowManager {
    private List<SetWindowEntity> list;
    private static ListSetWindowManager instance;

    public static ListSetWindowManager getInstance() {
        if (instance == null) {
            instance = new ListSetWindowManager();
        }
        return instance;
    }

    public void setListSet(List<SetWindowEntity> list) {
        this.list = list;
    }

    public List<SetWindowEntity> getListSet() {
        return list;
    }


    public SetWindowEntity getSetById(long id) {
        SetWindowEntity setWindowEntity = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getProductSetId() == id) {
                setWindowEntity = list.get(i);
                break;
            }
        }
        return setWindowEntity;

    }
}
