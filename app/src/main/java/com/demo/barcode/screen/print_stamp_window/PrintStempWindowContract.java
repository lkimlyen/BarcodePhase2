package com.demo.barcode.screen.print_stamp_window;

import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.ListPackCodeWindowModel;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface PrintStempWindowContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showOrderPackaging(SOEntity soEntity);

        void showTotalNumberScan(int sum);

        void showListScanPackaging(ListPackCodeWindowModel list);

        void showDialogCreateIPAddress();

        void startActivityCreate();

        void showProductSetName(String apartmentName);


        void showListCodePack(List<CodePackEntity> list);
    }

    interface Presenter extends BasePresenter {

        void getOrderPackaging(long orderId);


        void getListDetailPackageById(long mainId);

        void printTemp(long orderId, long productSetId, int direction,String packCode,int numberOnPack, long serverId, long logSerialId);

        void getSetWindow(long apartmentId);


        void saveIPAddress(String ipAddress, int port, long orderId, long productSetId, int direction,String packCode,int numberOnPack, long serverId, long logSerialId);


    }
}
