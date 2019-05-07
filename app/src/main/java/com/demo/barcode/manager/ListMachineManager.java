package com.demo.barcode.manager;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.MachineEntity;
import com.demo.barcode.app.CoreApplication;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;

public class ListMachineManager {
    private List<MachineEntity> list;
    private static ListMachineManager instance;

    public static ListMachineManager getInstance() {
        if (instance == null) {
            instance = new ListMachineManager();
        }
        return instance;
    }

    public void setListMachine(List<MachineEntity> list) {
        this.list = list;
        SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushListMachineObject(this.list);
    }




    public List<MachineEntity> getMachineByRole(int role) {

        list = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getListMachineObject();

        List<MachineEntity> machineList = new ArrayList<>();
        for (MachineEntity entity : list) {
            if (entity.getDepartmentId() == role) {
                machineList.add(entity);
            }
        }
        return machineList;
    }

}
