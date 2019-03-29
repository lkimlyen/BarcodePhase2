package com.demo.barcode.screen.history_pack_window;

import com.demo.architect.data.model.HistoryPackWindowEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.SetWindowEntity;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface HistoryPackWindowContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showListSO(List<SOEntity> list);

        void showListSetWindow(List<SetWindowEntity> list);

        void showListHistory(List<HistoryPackWindowEntity> list);
        void showDialogCreateIPAddress(long id);


    }

    interface Presenter extends BasePresenter {
        void getListSO();

        void getListSetWindow(long orderId);


        void getListHistory(long productSetId, int direction);

        void print(long id);
        void saveIPAddress(String ipAddress, int port, long id);



    }
}
