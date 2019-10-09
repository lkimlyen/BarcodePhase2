package com.demo.barcode.screen.confirm_receive_window;

import com.demo.architect.data.model.DeliveryNoteEntity;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogScanConfirmWindowModel;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public interface ConfirmReceiveWindowContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();

        void showListSO(List<SOEntity> list);

        void showListDepartment(List<DepartmentEntity> list);

        void showListConfirm(RealmResults<LogScanConfirmWindowModel> list);

        void setStatusPrint(boolean printed);

        void showDialogCreateIPAddress(boolean refresh);

        void showErrorByType(String message, int type);

        void showWarningPrint();

        void showListDeliveryCode(List<DeliveryNoteEntity> list);
    }

    interface Presenter extends BasePresenter {
        //lấy ds đơn hàng
        void getListSO();

        //lấy danh sách cần xác nhận từ server và lưu xuống databse local
        void getListConfirm(long maPhieuId, boolean refresh);

        //lấy ds mã phiếu giao
        void getListDeliveryCode(long orderId, int departmentIdOut);

        //lấy ds bộ phận
        void getListDepartment();

        //lấy danh sách cần xác nhận từ databse local
        void getListConfirm();

        //kiểm tra mã barcode và lưu chi tiết vào database
        void checkBarcode(String barcode);

        //cập nhật sl xác nhận
        void updateNumberConfirm(long outputId, int numberScan);

        //tải dữ liệu lên server
        void uploadData(long maPhieuId, long orderId, int departmentIdOut, int times, boolean checkPrint);

        //lưu địa chỉ ip để in
        void saveIPAddress(String ipAddress, int port, long maPhieuId, String maPhieu, long orderId, int departmentIdOut, int times, long serverId, boolean upload);

        //xác nhận tất cả chi tiết
        void confirmAll();

        //hủy xác nhận tất cả
        void cancelConfirmAll();
        //in các chi tiết đã xác nhận
        void print(long maPhieuId, long orderId, int departmentIdOut, int times, long serverId, boolean upload);

    }
}
