package com.demo.barcode.screen.stages;

import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public interface StagesContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showListDepartment(List<DepartmentEntity> list);

        void showListLogScanStages(RealmResults<LogScanStages> parent);

        void showListSO(List<SOEntity> list);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();

        void showCheckResidual();

        void showCheckResidualInGroup(long id, int number, int numberInput);

        void showListTimes(List<Integer> list);

        void showDialogUpload();

        void showDialogChooseGroup(List<GroupEntity> list);

        void refreshLayout();

        void showPrintDeliveryNote(int id);

        void showDialogCreateIPAddress(int idPrint);


    }

    interface Presenter extends BasePresenter {
        void getListDepartment();

        void getListSO(int orderType);

        void getListProduct(long orderId, boolean refresh);

        void checkBarcode(String barcode, int departmentId, int times, boolean groupCode);

        void deleteScanStages(long stagesId);

        void updateNumberScanInGroup(LogScanStages logScanStages, int numberInput);

        void getListTimes(long orderId);

        void uploadData(long orderId);

        void saveBarcodeToDataBase(int times, ProductDetail
                productDetail, int number, int departmentId, GroupEntity groupEntity, boolean typeScan, boolean residual);

        void getListGroupCode(long orderId);

        void saveListWithGroupCode(int times, GroupEntity groupEntity, int departmentId);

        void saveBarcodeWithGroup(GroupEntity groupEntity, int times, int departmentId);

        void countListAllData(long orderId);

        void updateNumberScan(long stagesId, int number, boolean update);

        void print(int id, int idPrint);

        void saveIPAddress(String ipAddress, int port, int idPrint);

        void deleteAllData();

        void getAllListStages();

    }
}
