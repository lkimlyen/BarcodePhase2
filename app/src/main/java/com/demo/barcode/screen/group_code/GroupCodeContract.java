package com.demo.barcode.screen.group_code;

import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.ListGroupCode;
import com.demo.architect.data.model.offline.LogScanStages;
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

        void showListScanStages(RealmResults<LogScanStages> results);

        void showGroupCode(RealmResults<ListGroupCode> groupCodes);

        void showSODetail(SOEntity soEntity);

        void backScanStages(String message);
    }

    interface Presenter extends BasePresenter {
        void getListModule(int orderId);

        void getListScanStages(int orderId, int departmentId, int times, String module);

        void getListGroupCode(int orderId, int departmentId, int times, String module);

        void getListOrderDetail(int orderId);

        void groupCode(int orderId, int departmentId, int times, String module, Collection<LogScanStages> list);

        void updateGroupCode(ListGroupCode groupCode, int orderId, int departmentId, int times, Collection<LogScanStages> list);

        void updateNumberGroup(int logId, int numberGroup);

        void detachedCode(int orderId, int departmentId, int times, ListGroupCode list);

        void removeItemInGroup(ListGroupCode groupCode, LogScanStages logScanStages, int orderId, int departmentId, int times);
    }
}
