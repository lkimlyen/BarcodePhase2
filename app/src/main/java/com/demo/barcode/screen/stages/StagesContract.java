package com.demo.barcode.screen.stages;

import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogListScanStages;
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

        void showListTimes(List<Integer> list);

        void clearDataNoProduct(boolean chooseType);
    }

    interface Presenter extends BasePresenter {
        void getListDepartment();

        void getListSO(int orderType);

        void getListProduct(int orderId);

        void checkBarcode(String barcode, int departmentId, int times);

        int countLogScanStages(int orderId, int departmentId,int times);

        void uploadData(int orderId, int departmentId, int times);

        void deleteScanStages(int stagesId);

        void updateNumberScanStages(int stagesId, int numberInput);

        void getListScanStages(int orderId, int departmentId, int times);

        void getListTimes(int orderId);

        void uploadDataAll(int orderId, int departmentId, int times);


    }
}
