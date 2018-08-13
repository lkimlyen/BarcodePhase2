package com.demo.barcode.manager;

import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;

import java.util.List;

public class ScanCreatePackManager {
    private LogScanCreatePackList packList;
    private static ScanCreatePackManager instance;

    public static ScanCreatePackManager getInstance() {
        if (instance == null) {
            instance = new ScanCreatePackManager();
        }
        return instance;
    }

    public void setPackList(LogScanCreatePackList user) {
        packList = user;
    }

    public LogScanCreatePackList getPackList() {
        return packList;
    }
    public List<LogScanCreatePack> getCreatePackList() {
        return packList.getItemList();
    }


}
