package com.demo.barcode.screen.print_stamp;

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

        void showCodePack(String codePack);
    }

    interface Presenter extends BasePresenter {

        void getOrderPackaging(int orderId);

        void getTotalScanBySerialPack(int orderId, int apartmentId, int moduleId, String serialPack);

        void getListScanStages(int orderId, int apartmentId, int moduleId, String serialPack);

        void printTemp(int serverId, String note);

        void getApartment(int apartmentId);

        void getModule(int moduleId);

        void saveIPAddress(String ipAddress, int port, int serverId, String note);

        void getCodePack(String serialPack);


    }
}
