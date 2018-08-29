package com.demo.barcode.manager;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.barcode.app.CoreApplication;

import java.util.List;

public class ListApartmentManager {
    private List<ApartmentEntity> list;
    private static ListApartmentManager instance;

    public static ListApartmentManager getInstance() {
        if (instance == null) {
            instance = new ListApartmentManager();
        }
        return instance;
    }

    public void setListDepartment(List<ApartmentEntity> list) {
        this.list = list;
    }

    public List<ApartmentEntity> getListApartment() {
        return list;
    }


}
