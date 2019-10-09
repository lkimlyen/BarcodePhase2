package com.demo.barcode.screen.group_code;

import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.GroupCode;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public interface GroupCodeContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showListModule(List<String> list);

        void showGroupCode(HashMap<String, List<ProductGroupEntity>> groupCodes);

        void showGroupCodeScanList(RealmResults<GroupCode> groupCodes);

        void showListSO(List<SOEntity> list);

        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();

        void refeshLayout();

    }

    interface Presenter extends BasePresenter {
        void getListModule(long orderId);

        //gộp mã các chi tiết lại vs nhau
        void groupCode(long orderId,  Collection<GroupCode> list);

        //lấy ds các chi tiết đã gộp
        void getListGroupCode(long orderId);

        //lấy ds các chi tiết đã quét
        void getGroupCodeScanList(long orderId);

        //cập nhật thêm chi tiết trong nhóm
        void updateGroupCode(String groupCode, long orderId, Collection<GroupCode> list);

        //cập  nhật số lượng gộp chi tiết trong  nhóm
        void updateNumberInGroup(String group, long orderId,Collection<ProductGroupEntity> listUpdate);

        //cập nhật số lượng gộp của nhóm
        void updateNumberGroup(long productId, long groupId, int numberGroup);

        //tách nhóm đã gộp
        void detachedCode(long orderId, String groupCode);

        //xóa chi tiết trong nhóm
        void removeItemInGroup(ProductGroupEntity logScanStages, long orderId);

        void getListSO(int orderType);

        void getListProduct(long orderId);

        void checkBarcode(String barcode);
        //lấy ds các chi tiết đã gộp
        void getListProductDetailInGroupCode(long orderId);

        void deleteScanGroupCode(long id);

        int totalNumberScanGroup(long productDetailId);


    }
}
