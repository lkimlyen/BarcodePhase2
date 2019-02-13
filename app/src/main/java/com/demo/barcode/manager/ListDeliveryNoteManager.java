package com.demo.barcode.manager;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.DeliveryNoteEntity;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.barcode.app.CoreApplication;

import java.util.List;

public class ListDeliveryNoteManager {
    private List<DeliveryNoteEntity> list;
    private static ListDeliveryNoteManager instance;

    public static ListDeliveryNoteManager getInstance() {
        if (instance == null) {
            instance = new ListDeliveryNoteManager();
        }
        return instance;
    }

    public void setListDeliveryNote(List<DeliveryNoteEntity> list) {
        this.list = list;
    }

    public String getDeliveryNoteById(long role) {
        String name = "";
        for (DeliveryNoteEntity entity : list) {
            if (entity.getId() == role) {
                name = entity.getNoteCode();
                break;
            }
        }
        return name;
    }

}
