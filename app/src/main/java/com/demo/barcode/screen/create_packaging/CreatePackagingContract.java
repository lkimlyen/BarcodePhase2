package com.demo.barcode.screen.create_packaging;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public interface CreatePackagingContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();

        void showListScan(RealmResults<LogListSerialPackPagkaging> log);

        void showListSO(List<SOEntity> list);

        void showListApartment(List<ApartmentEntity> list);

        void setHeightListView();
    }

    interface Presenter extends BasePresenter {

        void getListSO(int orderType);

        void getListApartment(long orderId);

        void getListScan();

        void deleteLogScan(long productId, long logId, String sttPack,String codePack);

        void updateNumberScan(long productId,long logId, int number,String sttPack, String codePack,int numberTotal);

        void checkBarcode(String barcode, long orderId, long apartmentId);

        void getListProduct(long orderId, long apartmentId);

        void deleteAllItemLog();
    }
}
