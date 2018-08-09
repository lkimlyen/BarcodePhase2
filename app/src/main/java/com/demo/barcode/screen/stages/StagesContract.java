package com.demo.barcode.screen.stages;

import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.NumberInput;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogListScanStages;
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

        void showListDepartment(List<DepartmentEntity> list);

        void showListLogScanStages(LogListScanStages parent);

        void showListSO(List<SOEntity> list);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();

        void showCheckResidual(int times);

        void showChooseTimes(List<NumberInput> list);
    }

    interface Presenter extends BasePresenter {
        void getListDepartment();

        void getListSO();

        void getListProduct(int orderId);

        void checkBarcode(String barcode, int orderId, int departmentId, double latitude, double longitude);

        int countLogScanStages(int orderId, int departmentId);

        void saveBarcode(NumberInput numberInput, String barcode, int orderId, int departmentId, double latitude, double longitude);
    }
}
