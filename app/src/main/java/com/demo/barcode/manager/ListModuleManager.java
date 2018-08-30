package com.demo.barcode.manager;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.barcode.app.CoreApplication;

import java.util.List;

public class ListModuleManager {
    private List<ModuleEntity> list;
    private static ListModuleManager instance;

    public static ListModuleManager getInstance() {
        if (instance == null) {
            instance = new ListModuleManager();
        }
        return instance;
    }

    public void setListModule(List<ModuleEntity> list) {
        this.list = list;

    }

    public ModuleEntity getModuleById(int id) {

        ModuleEntity moduleEntity = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getProductId() == id) {
                moduleEntity = list.get(i);
               break;
            }
        }
        return moduleEntity;
    }



}
