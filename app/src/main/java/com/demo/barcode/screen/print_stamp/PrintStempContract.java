package com.demo.barcode.screen.print_stamp;

import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
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

        void showOrderPackaging(SOEntity soEntity);

        void showTotalNumberScan(int sum);

        void showListScanPackaging(LogListSerialPackPagkaging list, boolean enough);

        void showDialogCreateIPAddress();

        void startActivityCreate();

        void showApartmentName(String apartmentName);

        void showModuleName(String module);

        void showSerialPack(PackageEntity packageEntity);

        void showListCodePack(List<CodePackEntity> list);
    }

    interface Presenter extends BasePresenter {

        void getOrderPackaging(long orderId);

        //lấy tổng sl chi tiết đã quét theo stt gói
        void getTotalScanBySerialPack(long orderId, long apartmentId, long moduleId, String serialPack);

        //lấy ds chi tiết gói theo id
        void getListDetailPackageById(long logSerialId);

        void printTemp(long orderId, long apartmentId, long moduleId, String serialPack, long serverId,long logSerialId);

        void getApartment(long apartmentId);

        //lấy tên module
        void getModule(long moduleId, String serialPack);

        void saveIPAddress(String ipAddress, int port, long orderId, long apartmentId, long moduleId, String serialPack, long serverId,long logSerialId);

        void getListCodePack(long orderId, int orderType, long productId);

        boolean checkNumberProduct( long productId, String sttPack);
    }
}
