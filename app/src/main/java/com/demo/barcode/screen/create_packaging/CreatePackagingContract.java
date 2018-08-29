package com.demo.barcode.screen.create_packaging;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogListModulePagkaging;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

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

        void showListScan(LogListSerialPackPagkaging log);

        void showListSO(List<SOEntity> list);

        void showListApartment(List<ApartmentEntity> list);

        void showListModule(List<ModuleEntity> list);

        void showListCodePack(List<CodePackEntity> list);

    }

    interface Presenter extends BasePresenter {

        void getListSO(int orderType);

        void getListApartment(int orderId);

        void getListModule(int orderId, int orderType, int apartmentId);

        void getListScan(int orderId, int productId, int apartmentId,String packcode,String sttPack);

        void deleteLogScan(int id);

        void updateNumberScan(int id, int number);

        void getListCodePack(int orderId, int orderType, int productId);

        void checkBarcode(String barcode, int orderId,int productId, int apartmentId, String packCode,String sttPack);
    }
}
