package com.demo.barcode.screen.print_stamp;

import com.demo.architect.data.model.offline.LogListModulePagkaging;
import com.demo.architect.data.model.offline.LogListOrderPackaging;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

/**
 * Created by MSI on 26/11/2017.
 */

public interface PrintStempContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showListSerialPack(LogListModulePagkaging log);

        void showOrderPackaging(LogListOrderPackaging log);

        void showTotalNumberScan(int sum);
    }

    interface Presenter extends BasePresenter {

        void getListSerialPack(int orderId, String floor, String module);

        void getOrderPackaging(int orderId);

        void getTotalScanBySerialPack(int logId);
    }
}
