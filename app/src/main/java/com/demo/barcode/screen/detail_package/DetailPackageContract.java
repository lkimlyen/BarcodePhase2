package com.demo.barcode.screen.detail_package;

import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.HistoryEntity;
import com.demo.architect.data.model.PackageEntity;
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

        void showListProductHistory(PackageEntity packageEntity);

        void startActivityHistory();

        void showApartmentName(String apartmentName);

        void showOrder(SOEntity soEntity);

        void showDialogCreateIPAddress();

        void showListCodePack(List<CodePackEntity> list);
    }

    interface Presenter extends BasePresenter {
        void getOrderPackaging(int orderId);

        void getListHistoryBySerialPack(HistoryEntity historyEntity, String serialPack);

        void printTemp(int serverId, int packageId);

        void getApartment(int apartmentId);

        void getListCodePack(int orderId, int orderType, int productId);

        void saveIPAddress(String ipAddress, int port, int serverId, int packageId);

    }
}
