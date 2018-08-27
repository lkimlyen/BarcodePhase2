package com.demo.barcode.screen.group_code;

import com.demo.architect.data.model.offline.GroupCode;
import com.demo.architect.data.model.offline.ListGroupCode;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

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
        void getListScanStages(RealmResults<LogScanStages> results);

        void showGroupCode(RealmResults<ListGroupCode> groupCodes);
    }

    interface Presenter extends BasePresenter {
        void getListModule(int orderId);

        void getListScanStages(int orderId, int departmentId, int times, String module);

        void getListGroupCode(int orderId, int departmentId, int times, String module);
    }
}
