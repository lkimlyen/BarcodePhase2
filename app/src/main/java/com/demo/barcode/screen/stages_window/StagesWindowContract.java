package com.demo.barcode.screen.stages_window;

import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.StaffEntity;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.LogScanStagesWindowModel;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.architect.data.model.offline.ProductDetailWindowModel;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public interface StagesWindowContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showListDepartment(List<DepartmentEntity> list);

        void showListLogScanStages(RealmResults<LogScanStagesWindowModel> parent);

        void showListSO(List<SOEntity> list);

        //mở âm thanh cảnh báo lỗi
        void startMusicError();

        //mở âm thanh thành công
        void startMusicSuccess();

        // tắt rung
        void turnOnVibrator();

        //show cảnh báo quét dư
        void showCheckResidual();

        void showDialogUpload();

        //cập nhật lại layout của reclycleview khi xóa, hoặc thêm chi tiết
        void refreshLayout();

        void showListStaff(List<StaffEntity> list);

        //show dialog hỏi ng dùng có muốn in những chi tiết vừa tải lên server không
        void showPrintDeliveryNote(int id);

        //show dialog thêm mới ip kết nối vs máy in
        void showDialogCreateIPAddress(int idPrint);

        void showErrorByType(String message, int type);
    }

    interface Presenter extends BasePresenter {
        //lấy ds bộ phận
        void getListDepartment();

        //lấy list đơn hàng
        void getListSO();

        //lấy ds nhân viên
        void getListStaff();

        //danh sách chi tiết trong đơn hàng
        void getListProduct(long orderId, boolean refresh);

        //kiểm tra mã barcode hợp lệ và lưu xuống database
        void checkBarcode(String barcode, int departmentId, int staffId);

        //xóa chi tiết quét
        void deleteScanStages(long stagesId);

        //tải ds chi tiết đã quét lên server
        void uploadData(long orderId, int departmentIn, int staffId);

        //lưu chi tiết vào database khi xác nhận quét dư
        void saveBarcodeToDataBase(ProductDetailWindowModel
                                           productDetail, int number, int departmentId, int staffId, boolean residual);

        //update số lượng chi tiết đã quét
        void updateNumberScan(long stagesId, int number, boolean update);

        //in ds chi tiết đã quét
        void print(int id, int idPrint);

        //lưu địa chỉ ip kết nối vs máy in
        void saveIPAddress(String ipAddress, int port, int idPrint);

        //xóa tất cả dữ liệu chưa upload
        void deleteAllData();

        //lấy ds dữ liệu chưa upload
        void getAllListStages();

    }
}
