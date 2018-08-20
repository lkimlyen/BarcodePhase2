package com.demo.barcode.screen.print_stamp;

import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

/**
 * Created by MSI on 26/11/2017.
 */

public interface PrintStempContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);

        void showSuccess(String message);
    }

    interface Presenter extends BasePresenter {

    }
}
