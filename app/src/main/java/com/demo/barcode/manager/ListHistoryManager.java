package com.demo.barcode.manager;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.HistoryEntity;

import java.util.List;

public class ListHistoryManager {
    private List<HistoryEntity> list;
    private static ListHistoryManager instance;

    public static ListHistoryManager getInstance() {
        if (instance == null) {
            instance = new ListHistoryManager();
        }
        return instance;
    }

    public void setListHitory(List<HistoryEntity> list) {
        this.list = list;
    }

    public List<HistoryEntity> getListHistory() {
        return list;
    }


    public HistoryEntity getHistoryById(int packageId) {
        HistoryEntity historyEntity = null;
        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getPackageId() == packageId) {
//                historyEntity = list.get(i);
//                break;
//            }
        }
        return historyEntity;

    }
}
