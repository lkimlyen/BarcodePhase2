package com.demo.barcode.screen.create_packaging;

import com.demo.architect.data.model.offline.LogListModulePagkaging;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

/**
 * Created by MSI on 26/11/2017.
 */

public interface CreatePackagingContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();

        void showListScan(LogListModulePagkaging logListModulePagkaging);
    }

    interface Presenter extends BasePresenter {

        void getListSO(int orderType);

        void getListDetail(int orderId);

        void getListFloor(int orderId);

        void getListModule(int orderId);

        void getListScan(int orderId, String floor, String module);

        void deleteLogScan(int id);

        void updateNumberScan(int id, int number);

        void checkBarcode(String barcode,int orderId, String floor, String module);

    }
}
