package com.demo.barcode.screen.detail_package;

import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogListOrderPackaging;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface DetailPackageContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();

        void showTotalNumberScan(int sum);

        void showListProductHistory(List<ProductPackagingEntity> list);

        void startActivityHistory();

        void showApartmentName(String apartmentName);

        void showModuleName(String module);

        void showCodePack(String codePack);

        void showOrder(SOEntity soEntity);
    }

    interface Presenter extends BasePresenter {
        void getOrderPackaging(int orderId);

        void getTotalScan(int packageId);

        void getListProductHistory(int packageId);

        void printTemp(int serverId, String note, int packageId);

        void getApartment(int apartmentId);

        void getModule(int moduleId);

        void getCodePack(String serialPack);
    }
}
