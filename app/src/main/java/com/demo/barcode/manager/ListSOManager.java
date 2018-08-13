package com.demo.barcode.manager;

import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class ListSOManager {
    private List<SOEntity> listSO = new ArrayList<>();
    private static ListSOManager instance;

    public static ListSOManager getInstance() {
        if (instance == null) {
            instance = new ListSOManager();
        }
        return instance;
    }

    public List<SOEntity> getListSO() {
        return listSO;
    }

    public void setListSO(List<SOEntity> listSO) {
        this.listSO = listSO;
    }

    public SOEntity getSOById(int id) {
        for (SOEntity so : listSO) {
            if (so.getOrderId() == id) {
                return so;
            }
        }
        return null;
    }

}
