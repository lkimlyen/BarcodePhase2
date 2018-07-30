package com.demo.barcode.screen.detail_package;

import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.offline.LogCompleteCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

/**
 * Created by MSI on 26/11/2017.
 */

public interface DetailPackageContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showOrder(OrderModel model);

        void showListCreatePack(LogCompleteCreatePackList list);

        void showDetailPack(LogCompleteCreatePackList pack);

        void showNumTotal(int num);

        void showDialogNumber(final ProductEntity productEntity, String barcode);

        void backToHistory(int request);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();
    }

    interface Presenter extends BasePresenter {
        void getOrder(int orderId);

        void getListHistory(int logId);

        void deleteCode(int id, int productId, int logId, int serial, int number);

        void deletePack(int logId, int orderId);

        void printStemp(int orderId, int serial, int serverId, int logId);

        void updateData(int logId, int orderId, int serial, boolean print);

        void checkBarcode(String barcode, int orderId, int logId);

        int countListScan(int logId);

        void saveBarcode(double latitude, double longitude, String barcode, int logId, int numberInput, int serial);

        void deleteCodeNotComplete(int logId);
    }
}
