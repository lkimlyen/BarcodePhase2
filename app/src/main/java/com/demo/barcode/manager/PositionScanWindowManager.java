package com.demo.barcode.manager;

import com.demo.architect.data.model.PositionScan;
import com.demo.architect.data.model.PositionScanWindow;

public class PositionScanWindowManager {
    private PositionScanWindow positionScan;
    private static PositionScanWindowManager instance;

    public static PositionScanWindowManager getInstance() {
        if (instance == null) {
            instance = new PositionScanWindowManager();
        }
        return instance;
    }

    public void setPositionScan(PositionScanWindow positionScan) {
        this.positionScan = positionScan;
        //   SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushListPositionScanObject(positionScan);
    }


    public PositionScanWindow getPositionScan() {

        //  positionScan = SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getListPositionScanObject();

        return positionScan;
    }


}
