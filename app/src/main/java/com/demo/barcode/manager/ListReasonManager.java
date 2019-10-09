package com.demo.barcode.manager;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.ReasonsEntity;
import com.demo.barcode.app.CoreApplication;

import java.util.List;

public class ListReasonManager {
    private List<ReasonsEntity> list;
    private static ListReasonManager instance;

    public static ListReasonManager getInstance() {
        if (instance == null) {
            instance = new ListReasonManager();
        }
        return instance;
    }

    public void setListReason(List<ReasonsEntity> list) {
        this.list = list;
    }

    public List<ReasonsEntity> getListReason() {
        return list;
    }



}
