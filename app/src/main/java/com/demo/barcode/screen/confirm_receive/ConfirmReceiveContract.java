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
        //lấy ds đơn hàng
        void getListSO(int orderType);

        //lấy số lần thưc hiện
        void getListTimes(long orderId);

        //lấy ds cần xác nhận theo mã phiếu và số lần thực hiện lưu xuống database local
        void getListConfirm(long maPhieuId, int times, boolean refresh);

        //lấy ds mã phiếu giao
        void getListDeliveryCode(long orderId, int departmentIdOut);

        //lấy ds bộ phận
        void getListDepartment();

        //lấy ds xác nhận theo số lần thực hiện
        void getListConfirm(int times);

        //kiểm tra mã barcode và lưu xuống database
        void checkBarcode(String barcode, boolean groupCode);

        //cập nhật số lượng xác nhận
        void updateNumberConfirm(long outputId, int numberScan);

        //tải dữ liệu lên server
        void uploadData(long maPhieuId, long orderId, int departmentIdOut, int times);

        //lấy danh sách chi tiết được gộp lại thành nhóm
        void getListGroupCode(long orderId);

        //lưu địa chỉ ip để connect đến máy in
        void saveIPAddress(String ipAddress, int port, long maPhieuId, long orderId, int departmentIdOut, int times, long serverId, boolean upload);

        //xác nhận tất cả chi tiết
        void confirmAll();

        //hủy chọn xác nhận tất cả
        void cancelConfirmAll();

        //xác nhận theo nhóm đã gộp
        void saveNumberConfirmGroup(GroupEntity groupEntity, long maPhieuId, long orderId, int times, int deparmentId);

        //in các chi tiết đã xác nhận
        void print(long maPhieuId, long orderId, int departmentIdOut, int times, long serverId, boolean upload);

    }
}
