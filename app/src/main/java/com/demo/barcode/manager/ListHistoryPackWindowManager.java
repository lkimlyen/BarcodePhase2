package com.demo.barcode.manager;

import com.demo.architect.data.model.HistoryEntity;
import com.demo.architect.data.model.HistoryPackWindowEntity;

import java.util.List;

public class ListHistoryPackWindowManager {
    private List<HistoryPackWindowEntity> list;
    private static ListHistoryPackWindowManager instance;

    public static ListHistoryPackWindowManager getInstance() {
        if (instance == null) {
            instance = new ListHistoryPackWindowManager();
        }
        return instance;
    }

    public void setListHitory(List<HistoryPackWindowEntity> list) {
        this.list = list;
    }

    public List<HistoryPackWindowEntity> getListHistory() {
        return list;
    }



}
