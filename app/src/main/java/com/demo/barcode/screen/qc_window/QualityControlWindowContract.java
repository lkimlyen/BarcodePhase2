package com.demo.barcode.screen.qc_window;

import com.demo.architect.data.model.MachineEntity;
import com.demo.architect.data.model.QCEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.QualityControlModel;
import com.demo.architect.data.model.offline.QualityControlWindowModel;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public interface QualityControlWindowContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();

        void showListMachine(List<MachineEntity> list);

        void showListQC(List<QCEntity> list);

        void showListQualityControl(RealmResults<QualityControlWindowModel> results);

        void showListSO(List<SOEntity> list);

    }

    interface Presenter extends BasePresenter {

        void getListSO();

        void getListMachine();

        void getListQC();

        void getListProduct(long orderId);

        void checkBarcode(String barcode, long orderId, int machineId, String violator, int qcId);

        void getListQualityControl();

        void removeItemQualityControl(long id);

        void uploadData(int machineId, String violator, int qcId, long orderId);

        void deleteAllQC();
    }
}
