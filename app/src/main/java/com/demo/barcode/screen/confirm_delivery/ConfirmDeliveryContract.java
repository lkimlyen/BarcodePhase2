package com.demo.barcode.screen.confirm_delivery;

import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.offline.ScanDeliveryList;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface ConfirmDeliveryContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);

        void showListRequest(List<OrderRequestEntity> list);

        void showListPackage(ScanDeliveryList list);
    }

    interface Presenter extends BasePresenter {
        void getRequest();

        void checkRequest(String codeRequest);

        void uploadData(String codeRequest);

    }
}
