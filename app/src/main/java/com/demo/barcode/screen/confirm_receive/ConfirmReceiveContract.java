package com.demo.barcode.screen.confirm_receive;

import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

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

        void showListConfirm(List<OrderConfirmEntity> list);
    }

    interface Presenter extends BasePresenter {
        void getListSO(int orderType);

        void getListTimes(int orderId);

        void getListConfirm(int orderId, int departmentIdIn, int departmentIdOut);

        void getListDepartment();
    }
}
