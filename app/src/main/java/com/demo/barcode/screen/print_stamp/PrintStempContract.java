package com.demo.barcode.screen.print_stamp;

import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.offline.LogListModulePagkaging;
import com.demo.architect.data.model.offline.LogListOrderPackaging;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface PrintStempContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showOrderPackaging(LogListOrderPackaging log);

        void showTotalNumberScan(int sum);

        void showListScanPackaging(List<LogScanPackaging> list);

        void showDialogCreateIPAddress();

        void startActivityCreate();

        void showApartmentName(String apartmentName);

        void showModuleName(String module);

        void showSerialPack(PackageEntity packageEntity);

        void showListCodePack(List<CodePackEntity> list);
    }

    interface Presenter extends BasePresenter {

        void getOrderPackaging(long orderId);

        void getTotalScanBySerialPack(long orderId, long apartmentId, long moduleId, String serialPack);

        void getListScanStages(long orderId, long apartmentId, long moduleId, String serialPack);

        void printTemp(long orderId, long apartmentId, long moduleId, String serialPack, long serverId);

        void getApartment(long apartmentId);

        void getModule(long moduleId, String serialPack);

        void saveIPAddress(String ipAddress, int port, long orderId, long apartmentId, long moduleId, String serialPack, long serverId);

        void getListCodePack(long orderId, int orderType, long productId);

        boolean checkNumberProduct(long orderId, long productId, long apartmentId, String sttPack);
    }
}
