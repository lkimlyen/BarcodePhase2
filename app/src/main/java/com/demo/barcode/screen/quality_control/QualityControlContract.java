package com.demo.barcode.screen.quality_control;

import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.architect.data.model.offline.QualityControlModel;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public interface QualityControlContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();
        void showListDepartment(List<DepartmentEntity> list);

        void showListQualityControl(RealmResults<QualityControlModel> results);
        void showListSO(List<SOEntity> list);

    }

    interface Presenter extends BasePresenter {

        void getListDepartment();

        void getListSO(int orderType);

        void getListProduct(int orderId,int departmentId);


        void checkBarcode(String barcode, int orderId, int departmentId);

        void getListQualityControl(int orderId, int departmentId);
        void removeItemQualityControl(int id);

        void uploadData();
    }
}
