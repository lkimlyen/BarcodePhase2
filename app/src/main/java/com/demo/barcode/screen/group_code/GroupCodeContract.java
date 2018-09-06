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

        void showGroupCode(RealmResults<ListGroupCode> groupCodes);
        void showGroupCodeScanList(RealmResults<GroupCode> groupCodes);

        void showListSO(List<SOEntity> list);

        void backScanStages(String message);
        void startMusicError();

        void startMusicSuccess();

        void turnOnVibrator();


    }

    interface Presenter extends BasePresenter {
        void getListModule(int orderId);

        void groupCode(int orderId,  String module, Collection<GroupCode> list);
        void getListGroupCode(int orderId, String module);

        void getGroupCodeScanList(int orderId, String module);

        void updateGroupCode(ListGroupCode groupCode, int orderId, int departmentId, int times, Collection<LogScanStages> list);

        void updateNumberGroup(int productId,int groupId, int numberGroup);

        void detachedCode(int orderId, int departmentId, int times, ListGroupCode list);

        void removeItemInGroup(String groupCode, LogScanStages logScanStages, int orderId, int departmentId, int times);

        void getListSO(int orderType);

        void getListProduct(int orderId);

        void checkBarcode(String barcode, String module);
    }
}
