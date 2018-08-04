package com.demo.barcode.screen.stages;

import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface StagesContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showRequestProduction(List<OrderModel> list);

        void showLogScanCreatePack(LogScanCreatePackList list);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();
    }

    interface Presenter extends BasePresenter {
        void getData();

        void getRequestProduction();

        void getProduct(int orderId);

        void checkBarcode(String barcode, int orderId, double latitude, double longitude);

        void getListCreateCode(int orderId);

        void deleteItemLog(LogScanCreatePack item);

        void updateNumberInput(int id, int number, int serial, int currentNumber);

        void deleteAllItemLog();

        int countListScan(int orderId);

    }
}
