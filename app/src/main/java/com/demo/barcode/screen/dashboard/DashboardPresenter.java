package com.demo.barcode.screen.dashboard;

import androidx.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.barcode.manager.UserManager;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class DashboardPresenter implements DashboardContract.Presenter {

    private final String TAG = DashboardPresenter.class.getName();
    private final DashboardContract.View view;

    @Inject
    LocalRepository localRepository;

    @Inject
    DashboardPresenter(@NonNull DashboardContract.View view) {
        this.view = view;
    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void start() {
        Log.d(TAG, TAG + ".start() called");
        getUser();

    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    @Override
    public void getUser() {
        UserEntity user = UserManager.getInstance().getUser();
        view.showUser(user);
    }

    @Override
    public void logout() {
        UserManager.getInstance().setUser(null);
    }




}
