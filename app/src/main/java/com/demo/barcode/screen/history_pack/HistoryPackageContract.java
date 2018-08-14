package com.demo.barcode.screen.history_pack;

import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

/**
 * Created by MSI on 26/11/2017.
 */

public interface HistoryPackageContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);
    }

    interface Presenter extends BasePresenter {
    }
}
