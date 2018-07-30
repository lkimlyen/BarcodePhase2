package com.demo.barcode.screen.print_stemp;

import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

/**
 * Created by MSI on 26/11/2017.
 */

public interface PrintStempContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showSerialPack(int serial);

        void showOrder(OrderModel model);

        void showListCreatePack(LogScanCreatePackList list);

        void showSumPack(int sum);

        void backToCreatePack();

        void showDialogCreateIPAddress();
    }

    interface Presenter extends BasePresenter {
        void getMaxNumberOrder(int orderId);

        void getOrder(int order);

        void getListCreatePack(int orderId);

        void printStemp(int orderId, int serial, int serverId, int numTotal);

        void deleteAllLog();

        void saveIPAddress(String ipAddress, int port,int orderId, int serial, int serverId, int numTotal);
    }
}
