package com.demo.barcode.screen.detail_package;

import com.demo.architect.data.model.SOEntity;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

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

        void startActivityHistory();

        void showApartmentName(String apartmentName);

        void showOrder(SOEntity soEntity);

        void showDialogCreateIPAddress();
    }

    interface Presenter extends BasePresenter {
        void getOrderPackaging(long orderId);

        void printTemp(long serverId, long packageId);

        void getApartment(long apartmentId);

        void saveIPAddress(String ipAddress, int port, long serverId, long packageId);

    }
}
