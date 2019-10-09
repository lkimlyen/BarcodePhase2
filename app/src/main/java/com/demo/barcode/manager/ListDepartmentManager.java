package com.demo.barcode.manager;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.barcode.app.CoreApplication;

import java.util.ArrayList;
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
        List<DepartmentEntity> departmentEntityList = new ArrayList<>();

        for (DepartmentEntity entity : list) {

            if (entity.getId() != role && entity.getOrderType() == -1) {
                departmentEntityList.add(entity);
            }
        }

        return departmentEntityList;
    }

    public List<DepartmentEntity> getListDepartmentWithOrderType(int role, int orderType) {
        list = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getListDepartmentObject();
        List<DepartmentEntity> departmentEntityList = new ArrayList<>();

        for (DepartmentEntity entity : list) {

            if (entity.getId() != role && entity.getOrderType() == orderType) {
                departmentEntityList.add(entity);
            }
        }

        return departmentEntityList;
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
