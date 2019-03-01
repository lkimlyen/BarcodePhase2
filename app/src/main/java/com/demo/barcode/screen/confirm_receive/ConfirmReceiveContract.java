package com.demo.barcode.screen.confirm_receive;

import com.demo.architect.data.model.DeliveryNoteEntity;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogScanConfirmModel;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public interface ConfirmReceiveContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();

        void showListSO(List<SOEntity> list);

        void showListTimes(List<Integer> list);

        void showListDepartment(List<DepartmentEntity> list);

        void showListConfirm(RealmResults<LogScanConfirmModel> list);

        void setValuePrint(boolean printed);

        void showDialogCreateIPAddress(boolean refresh);

        void showDialogChooseGroup(List<GroupEntity> listGroupEntityByProductId);


        void showListDeliveryCode(List<DeliveryNoteEntity> list);
    }

    interface Presenter extends BasePresenter {
        void getListSO(int orderType);

        void getListTimes(long orderId, int departmentId);

        void getListConfirm(long maPhieuId, int times, boolean refresh);

        void getListDeliveryCode(long orderId, int departmentIdOut);

        void getListDepartment();

        void getListConfirm(int times);


        void checkBarcode(String barcode, boolean groupCode);

        void updateNumberConfirm(long outputId, int numberScan);

        void uploadData(long maPhieuId, long orderId, int departmentIdOut, int times);

        void getListGroupCode(long orderId);

        void saveIPAddress(String ipAddress, int port, long maPhieuId, long orderId, int departmentIdOut, int times, long serverId, boolean upload);

        void confirmAll();

        void cancelConfirmAll();

        void saveNumberConfirmGroup(GroupEntity groupEntity, long maPhieuId, long orderId, int times, int deparmentId);

        void print(long maPhieuId, long orderId, int departmentIdOut, int times, long serverId, boolean upload);

    }
}
