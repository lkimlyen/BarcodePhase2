package com.demo.barcode.screen.confirm_receive;

import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.ProductGroupEntity;
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

        void showDialogConfirm(List<ProductGroupEntity> list,int times);

        void setCheckedAll(boolean checkedAll);

    }

    interface Presenter extends BasePresenter {
        void getListSO(int orderType);

        void getListTimes(long orderId, int departmentId);

        void getListConfirm(long orderId, int departmentIdOut, int times, boolean refresh);

        void getListDepartment();

        void getListConfirmByTimes(long orderId, int deparmentId, int times);

        int countListConfirmByTimesWaitingUpload(long orderId, int deparmentId, int times);

        void checkBarcode(long orderId, String barcode, int departmentId, int times, boolean groupCode);

        void updateNumberConfirm(long orderId, long masterOutputId, int departmentIdOut, int times, double numberScan);

        void uploadData(long orderId, int departmentIdOut, int times);

        void getListGroupCode(long orderId);

        void confirmAll(long orderId, int departmentId, int times);

        void cancelConfirmAll(long orderId, int departmentId, int times);

        void saveListWithGroupCodeEnough(int times, List<ProductGroupEntity> list);

    }
}
