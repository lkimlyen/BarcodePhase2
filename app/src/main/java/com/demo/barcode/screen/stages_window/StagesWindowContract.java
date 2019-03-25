package com.demo.barcode.screen.stages_window;

import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.StaffEntity;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.LogScanStagesWindowModel;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.architect.data.model.offline.ProductDetailWindowModel;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public interface StagesWindowContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showListDepartment(List<DepartmentEntity> list);

        void showListLogScanStages(RealmResults<LogScanStagesWindowModel> parent);

        void showListSO(List<SOEntity> list);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();

        void showCheckResidual();

        void showDialogUpload();

        void refreshLayout();

        void showListStaff(List<StaffEntity> list);

        void showPrintDeliveryNote(int id);

        void showDialogCreateIPAddress(int idPrint);

        void showErrorByType(String message, int type);
    }

    interface Presenter extends BasePresenter {
        void getListDepartment();

        void getListSO();

        void getListStaff();

        void getListProduct(long orderId, boolean refresh);

        void checkBarcode(String barcode, int departmentId,int staffId);

        void deleteScanStages(long stagesId);

        void uploadData(long orderId, int departmentIn, int staffId);

        void saveBarcodeToDataBase(ProductDetailWindowModel
                                           productDetail, int number, int departmentId,int staffId, boolean residual);


        void updateNumberScan(long stagesId, int number, boolean update);

        void print(int id, int idPrint);

        void saveIPAddress(String ipAddress, int port, int idPrint);


        void deleteAllData();

        void getAllListStages();

    }
}
