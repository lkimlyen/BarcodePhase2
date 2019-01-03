package com.demo.barcode.screen.confirm_receive;

import com.demo.architect.data.model.DeliveryNoteEntity;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogScanConfirm;
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

        void showListConfirm(RealmResults<LogScanConfirm> list);

        void clearDataNoProduct(boolean chooseType);

        void showDialogConfirm(List<ProductGroupEntity> list, int times);

        void setCheckedAll(boolean checkedAll);

        void showDialogCreateIPAddress(boolean refresh);

        void showDialogChooseGroup(List<GroupEntity> listGroupEntityByProductId);

        void showWarningPrint();

        void showListDeliveryCode(List<DeliveryNoteEntity> list);
    }

    interface Presenter extends BasePresenter {
        void getListSO(int orderType);

        void getListTimes(long orderId, int departmentId);

        void getListConfirm(long maPhieuId, long orderId, int departmentIdOut, int times, boolean refresh);

        void getListDeliveryCode(long orderId, int departmentIdOut);

        void getListDepartment();

        void getListConfirmByTimes(long  deliveryNoteId,long orderId, int deparmentId, int times);

        int countListConfirmByTimesWaitingUpload(long orderId, int deparmentId, int times);

        void checkBarcode(long maPhieuId,long orderId, String barcode, int departmentId, int times, boolean groupCode);

        void updateNumberConfirm(long maPhieuId,long orderId, long masterOutputId, int departmentIdOut, int times, double numberScan);

        void uploadData(long maPhieuId,String maPhieu,long orderId, int departmentIdOut, int times, boolean checkPrint);

        void getListGroupCode(long orderId);

        void saveIPAddress(String ipAddress, int port,  long maPhieuId,String maPhieu, long orderId, int departmentIdOut, int times, long serverId, boolean upload);

        void confirmAll(long orderId, int departmentId, int times);

        void cancelConfirmAll(long orderId, int departmentId, int times);

        void saveListWithGroupCodeEnough(long maPhieuId,int times, List<ProductGroupEntity> list);

        void saveNumberConfirmGroup(GroupEntity groupEntity,long maPhieuId, long orderId, int times, int deparmentId);

        void print(long maPhieuId,String maPhieu,long orderId, int departmentIdOut, int times, long serverId, boolean upload);

    }
}
