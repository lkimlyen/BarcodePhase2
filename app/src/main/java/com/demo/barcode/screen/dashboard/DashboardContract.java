package com.demo.barcode.screen.dashboard;

import com.demo.architect.data.model.UserEntity;
import com.demo.barcode.app.base.BasePresenter;
import com.demo.barcode.app.base.BaseView;

/**
 * Created by MSI on 26/11/2017.
 */

public interface DashboardContract {
    interface View extends BaseView<Presenter> {
        void showUser(UserEntity user);
    }

    interface Presenter extends BasePresenter {
        void getUser();
        void logout();
    }
}
