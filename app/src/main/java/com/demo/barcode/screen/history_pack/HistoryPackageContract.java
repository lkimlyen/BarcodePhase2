package com.demo.barcode.screen.history_pack;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.HistoryEntity;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public interface HistoryPackageContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showListSO(List<SOEntity> list);

        void showListApartment(List<ApartmentEntity> list);

        void showListHistory(List<HistoryEntity> list);

    }

    interface Presenter extends BasePresenter {
        void getListSO(int orderType);

        void getListApartment(int orderId);

        void getListHistory(int orderId,int apartmentId);


    }
}
