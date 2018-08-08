package com.demo.barcode.manager;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.UserResponse;
import com.demo.barcode.app.CoreApplication;

import java.util.List;

public class ListDepartmentManager {
    private List<DepartmentEntity> list;
    private static ListDepartmentManager instance;

    public static ListDepartmentManager getInstance() {
        if (instance == null) {
            instance = new ListDepartmentManager();
        }
        return instance;
    }

    public void setListDepartment(List<DepartmentEntity> list) {
        this.list = list;
        SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushListDepartmentObject(this.list);
    }

    public List<DepartmentEntity> getListDepartment() {
        if (list == null) {
            list = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getListDepartmentObject();
        }
        return list;
    }

}
