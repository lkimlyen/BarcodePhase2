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
        void getOrderPackaging(long orderId);

        void getListHistoryBySerialPack(HistoryEntity historyEntity, String serialPack);

        void printTemp(long serverId, long packageId);

        void getApartment(long apartmentId);

        void getListCodePack(long orderId, int orderType, long productId);

        void saveIPAddress(String ipAddress, int port, long serverId, long packageId);

    }
}
