package com.demo.barcode.manager;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.DepartmentEntity;
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

    public List<DepartmentEntity> getListDepartment(int role) {
        list = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getListDepartmentObject();
        int position = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == role) {
                position = i;
                break;
            }
        }
        if (position > -1) {
            list.remove(position);
        }

        return list;
    }

    public String getDepartmentByRole(int role) {
        String name = "";
        list = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getListDepartmentObject();


        for (DepartmentEntity entity : list) {
            if (entity.getId() == role) {
                name = entity.getName();
                break;
            }
        }
        return name;
    }

}
