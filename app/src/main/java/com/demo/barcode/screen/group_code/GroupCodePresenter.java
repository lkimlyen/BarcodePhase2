package com.demo.barcode.screen.group_code;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.offline.GroupCode;
import com.demo.architect.data.model.offline.ListGroupCode;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.barcode.manager.ListProductManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class GroupCodePresenter implements GroupCodeContract.Presenter {

    private final String TAG = GroupCodePresenter.class.getName();
    private final GroupCodeContract.View view;
    @Inject
    LocalRepository localRepository;

    @Inject
    GroupCodePresenter(@NonNull GroupCodeContract.View view) {
        this.view = view;

    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);

    }


    @Override
    public void start() {
        Log.d(TAG, TAG + ".start() called");

    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    @Override
    public void getListModule(int orderId) {
        List<ProductEntity> list = ListProductManager.getInstance().getListProduct();
        List<String> result = new ArrayList<>();
        for (ProductEntity product : list) {
            if (!result.contains(product.getModule())){
                result.add(product.getModule());
            }
        }
        view.showListModule(result);
    }

    @Override
    public void getListScanStages(int orderId, int departmentId, int times, String module) {
        localRepository.getListScanStages(orderId,departmentId,times,module).subscribe(new Action1<RealmResults<LogScanStages>>() {
            @Override
            public void call(RealmResults<LogScanStages> logScanStages) {

            }
        });
    }

    @Override
    public void getListGroupCode(int orderId, int departmentId, int times, String module) {
        localRepository.getListGroupCode(orderId,departmentId,times,module).subscribe(new Action1<RealmResults<ListGroupCode>>() {
            @Override
            public void call(RealmResults<ListGroupCode> groupCodes) {
           view.showGroupCode(groupCodes);
            }
        });
    }
}
