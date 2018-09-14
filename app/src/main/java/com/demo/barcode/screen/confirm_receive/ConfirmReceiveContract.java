package com.demo.barcode.screen.confirm_receive;

import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogScanConfirm;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public interface ConfirmReceiveContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();

        void showListSO(List<SOEntity> list);

        void showListTimes(List<Integer> list);

        void showListDepartment(List<DepartmentEntity> list);

        void showListConfirm(RealmResults<LogScanConfirm> list);

        void clearDataNoProduct(boolean chooseType);

    }

    interface Presenter extends BasePresenter {
        void getListSO(int orderType);

        void getListTimes(int orderId);

        void getListConfirm(int orderId, int departmentIdOut);

        void getListDepartment();

        void getListConfirmByTimes(int orderId, int deparmentId, int times);

        int countListConfirmByTimesWaitingUpload(int orderId, int deparmentId, int times);

        void checkBarcode(int orderId, String barcode, int departmentId, int times, boolean groupCode);

        void updateNumberConfirm(int orderId, int masterOutputId, int departmentIdOut, int times, int numberScan);

        void uploadData(int orderId, int departmentIdOut, int times);

        void getListGroupCode(int orderId);

        void confirmAll(int orderId, int departmentId, int times);

        void cancelConfirmAll(int orderId, int departmentId, int times);
    }
}
