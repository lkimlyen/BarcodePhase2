package com.demo.barcode.screen.stages;

import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.GroupCode;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.architect.data.model.offline.LogScanStages;
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

        void showCheckResidual(int times, ProductEntity
                productEntity, int departmentId);

        void showCheckResidualInGroup(long id, double number, double numberInput);

        void showListTimes(List<Integer> list);

        void clearDataNoProduct(boolean chooseType);

        void showDialogUpload();

        void showDialogChooseGroup(List<GroupEntity> list);

    }

    interface Presenter extends BasePresenter {
        void getListDepartment();

        void getListSO(int orderType);

        void getListProduct(long orderId, int times, int department, boolean refresh);

        void checkBarcode(String barcode, int departmentId, int times, boolean groupCode);

        int countLogScanStages(long orderId, int departmentId, int times);

        void deleteScanStages(long stagesId);

        void updateNumberScanInGroup(LogScanStages logScanStages, double numberInput);

        void getListScanStages(long orderId, int departmentId, int times);

        void getListTimes(long orderId, int departmentId);

        void uploadDataAll(long orderId, int departmentId, int times);

        void saveBarcodeToDataBase(int times, ProductEntity
                productEntity, double number, int departmentId, GroupEntity groupEntity, boolean typeScan);

        void getListGroupCode(long orderId);

        void saveListWithGroupCode(int times, GroupEntity groupEntity, int departmentId);

        void saveBarcodeWithGroup(GroupEntity groupEntity, int times, int departmentId);

        void countListAllData(long orderId);

        void updateNumberScan(long stagesId, double number, boolean update);
    }
}
