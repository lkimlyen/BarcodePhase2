package com.demo.barcode.screen.group_code;

import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.GroupCode;
import com.demo.architect.data.model.offline.ListGroupCode;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.NumberInputModel;
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
        void getListModule(int orderId);

        void groupCode(int orderId, String module, Collection<GroupCode> list);

        void getListGroupCode(int orderId, String module);

        void getGroupCodeScanList(int orderId, String module);

        void updateGroupCode(String groupCode, int orderId, String module, Collection<GroupCode> list);

        void updateNumberInGroup(String group, int orderId, String module,Collection<ProductGroupEntity> listUpdate);

        void updateNumberGroup(int productId, int groupId, int numberGroup);

        void detachedCode(int orderId, String module, String groupCode);

        void removeItemInGroup(ProductGroupEntity logScanStages, int orderId, String module);

        void getListSO(int orderType);

        void getListProduct(int orderId);

        void checkBarcode(String barcode, String module);

        void getListProductDetailInGroupCode(int orderId, String module);
    }
}
