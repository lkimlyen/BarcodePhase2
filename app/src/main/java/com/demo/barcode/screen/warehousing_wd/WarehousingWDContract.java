package com.demo.barcode.screen.warehousing_wd;

import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.SOWarehouseEntity;
import com.demo.architect.data.model.StaffEntity;
import com.demo.architect.data.model.offline.LogScanStagesWindowModel;
import com.demo.architect.data.model.offline.ProductDetailWindowModel;
import com.demo.architect.data.model.offline.ProductWarehouseModel;
import com.demo.architect.data.model.offline.WarehousingModel;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public interface WarehousingWDContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showListWarehousing(RealmResults<WarehousingModel> parent);

        void showListSO(List<SOWarehouseEntity> list);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();

        void showCheckResidual();

        void showDialogUpload();

    }

    interface Presenter extends BasePresenter {

        void getListSO();

        void getListProduct(long orderId, boolean refresh);

        void checkBarcode(long orderId,String barcode);

        void deleteItem(long id);

        void uploadData(long orderId);

        void saveBarcodeToDataBase(long orderId, String barcode, ProductWarehouseModel
                productDetail, int number, int numberPack, boolean residual);


        void updateNumberScan(long id, int number, boolean update);


        void deleteAllData();

        void getAllListStages();

    }
}
