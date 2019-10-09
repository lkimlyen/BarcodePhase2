package com.demo.barcode.manager;

import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.PositionScan;
import com.demo.barcode.app.CoreApplication;

import java.util.LinkedHashMap;

public class PositionScanManager {
    private PositionScan positionScan;
    private static PositionScanManager instance;

    public static PositionScanManager getInstance() {
        if (instance == null) {
            instance = new PositionScanManager();
        }
        return instance;
    }

    public void setPositionScan(PositionScan positionScan) {
        this.positionScan = positionScan;
        //   SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushListPositionScanObject(positionScan);
    }


    public PositionScan getPositionScan() {

        //  positionScan = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getListPositionScanObject();

        return positionScan;
    }


}
