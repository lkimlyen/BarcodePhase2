package com.demo.barcode.manager;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.PositionScan;
import com.demo.barcode.app.CoreApplication;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListPositionScanManager {
    private LinkedHashMap<Long, LinkedHashMap<Long, PositionScan>> list = new LinkedHashMap<>();
    private static ListPositionScanManager instance;

    public static ListPositionScanManager getInstance() {
        if (instance == null) {
            instance = new ListPositionScanManager();
        }
        return instance;
    }

    public void setList(LinkedHashMap<Long, LinkedHashMap<Long, PositionScan>> list) {
        this.list = list;
        SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushListPositionScanObject(list);
    }

    public LinkedHashMap<Long, LinkedHashMap<Long, PositionScan>> getList() {
        this.list = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getListPositionScanObject();
        return list;
    }


    public PositionScan getPositionScanByOrderId(long orderId, long apartmentId) {

        list = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getListPositionScanObject();
        if (list != null) {
            if (list.get(orderId) != null) {
                if (list.get(orderId).get(apartmentId) != null) {
                    return list.get(orderId).get(apartmentId);
                }
            }


        }

        return null;
    }


    public int getPositionByOrderId(long orderId, long departmentId) {
        list = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getListPositionScanObject();
        if (list != null) {
            if (list.get(orderId) != null) {
                if (list.get(orderId).get(departmentId) != null) {

                }
            }


        }

        return -1;
    }

    public void removeList(long orderId, long apartmentId) {
        list = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getListPositionScanObject();
        if (list != null) {
            if (list.get(orderId) != null) {
                list.get(orderId).remove(apartmentId);
            }
        }
        SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushListPositionScanObject(list);
    }

    public void addPositionScan(long orderId, long apartmentId, PositionScan positionScan) {
        list = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getListPositionScanObject();
        if (list == null) {
            list = new LinkedHashMap<>();
        }
        LinkedHashMap<Long, PositionScan> listApartment = new LinkedHashMap<>();
        if (list.get(orderId) != null) {
            listApartment.putAll(list.get(orderId));
        }
        listApartment.put(apartmentId, positionScan);
        list.put(orderId, listApartment);
        SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushListPositionScanObject(list);

    }
}
