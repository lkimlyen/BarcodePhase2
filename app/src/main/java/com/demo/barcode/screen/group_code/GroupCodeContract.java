package com.demo.barcode.screen.group_code;

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

        void setHeightListView();

    }

    interface Presenter extends BasePresenter {
        void getListModule(long orderId);

        void groupCode(long orderId, String module, Collection<GroupCode> list);

        void getListGroupCode(long orderId);

        void getGroupCodeScanList(long orderId);

        void updateGroupCode(String groupCode, long orderId, Collection<GroupCode> list);

        void updateNumberInGroup(String group, long orderId,Collection<ProductGroupEntity> listUpdate);

        void updateNumberGroup(long productId, long groupId, double numberGroup);

        void detachedCode(long orderId, String groupCode);

        void removeItemInGroup(ProductGroupEntity logScanStages, long orderId);

        void getListSO(int orderType);

        void getListProduct(long orderId);

        void checkBarcode(String barcode);

        void getListProductDetailInGroupCode(long orderId);

        void deleteScanGroupCode(long id);
    }
}
