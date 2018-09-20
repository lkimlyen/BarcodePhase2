package com.demo.barcode.screen.stages;

import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.GroupCodeEntity;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.ListGroupCode;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.architect.data.model.offline.NumberInputModel;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

import io.realm.RealmList;

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

        void showCheckResidualInGroup(int times, List<ProductGroupEntity>
                productGroupEntityList, int departmentId);

        void showListTimes(List<Integer> list);

        void clearDataNoProduct(boolean chooseType);

        void showDialogUpload();

    }

    interface Presenter extends BasePresenter {
        void getListDepartment();

        void getListSO(int orderType);

        void getListProduct(int orderId, boolean refresh);

        void checkBarcode(String barcode, int departmentId, int times, boolean groupCode);

        int countLogScanStages(int orderId, int departmentId, int times);

        void deleteScanStages(int stagesId);

        void updateNumberScanStages(int stagesId, int numberInput, boolean update);

        void getListScanStages(int orderId, int departmentId, int times);

        void getListTimes(int orderId, int departmentId);

        void uploadDataAll(int orderId, int departmentId, int times);

        void saveBarcodeToDataBase(int times, ProductEntity
                productEntity, int number, int departmentId, GroupEntity groupEntity, boolean typeScan);

        void getListGroupCode(int orderId);

        void saveListWithGroupCode(int times, GroupEntity groupEntity, int departmentId);

        void saveListWithGroupCodeEnough(int times, List<ProductGroupEntity> list, int departmentId);

        void countListAllData(int orderId);
    }
}
