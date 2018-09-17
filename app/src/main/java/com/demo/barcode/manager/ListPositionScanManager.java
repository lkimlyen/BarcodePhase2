package com.demo.barcode.manager;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.PositionScan;
import com.demo.barcode.app.CoreApplication;

import java.util.ArrayList;
import java.util.List;

public class ListPositionScanManager {
    private List<PositionScan> list = new ArrayList<>();
    private static ListPositionScanManager instance;

    public static ListPositionScanManager getInstance() {
        if (instance == null) {
            instance = new ListPositionScanManager();
        }
        return instance;
    }

    public void setList(List<PositionScan> list) {
        this.list = list;
        SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushListPositionScanObject(list);
    }

    public List<PositionScan> getList() {
        this.list = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getListPositionScanObject();
        return list;
    }


    public PositionScan getPositionScanByOrderId(int orderId, int departmentId) {

        if (list == null) {
            list = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getListPositionScanObject();
        }
        PositionScan moduleEntity = null;
        if (list != null){
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getOrderId() == orderId) {
                    moduleEntity = list.get(i);
                    break;
                }
            }
        }

        return moduleEntity;
    }


}
