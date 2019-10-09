package com.demo.barcode.screen.create_pack_window;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.GroupSetEntity;
import com.demo.architect.data.model.ProductPackagingWindowEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.SetWindowEntity;
import com.demo.architect.data.model.offline.ListPackCodeWindowModel;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public interface CreatePackagingWindowContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();

        void showListScan(RealmResults<ListPackCodeWindowModel> results);

        void showListSO(List<SOEntity> list);

        void showListSetWindow(List<SetWindowEntity> list);

        void setHeightListView();

        void showDialogChooseSet(ProductPackagingWindowEntity entity);
    }

    interface Presenter extends BasePresenter {

        //lấy ds đơn hàng
        void getListSO();

        //lấy ds bộ cửa theo đơn hàng
        void getListSetWindow(long orderId);

        //lấy ds đã quét
        void getListScan();

        //lấy ds chi tiết trong bộ cửa by hướng
        void getListDetailInSet(long productSetId, int direction);

        //xóa chi tiết đã quét
        void deleteLogScan(long logId, String packCode, int numberOnPack);

        //cập nhật số lượng quét
        void updateNumberScan(long logId, int number, String packCode, int numberOnPack, int totalNumber);

        //kiểm tra mã barcode và lưu vào database
        void checkBarcode(String barcode, int direction);

        //xóa tất cả chi tiết đã quét
        void deleteAllItemLog();

        //lưu  mã barcode vs nhóm bộ cửa
        void saveBarcode(ProductPackagingWindowEntity entity, int direction, GroupSetEntity groupSetEntity);
    }
}
