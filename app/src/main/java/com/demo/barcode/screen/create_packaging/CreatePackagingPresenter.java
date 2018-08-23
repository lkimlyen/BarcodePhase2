package com.demo.barcode.screen.create_packaging;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.offline.LogListModulePagkaging;
import com.demo.architect.data.model.offline.ProductPackagingModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListProductPackagingManager;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class CreatePackagingPresenter implements CreatePackagingContract.Presenter {

    private final String TAG = CreatePackagingPresenter.class.getName();
    private final CreatePackagingContract.View view;
    @Inject
    LocalRepository localRepository;

    @Inject
    CreatePackagingPresenter(@NonNull CreatePackagingContract.View view) {
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
    public void getListSO(int orderType) {

    }

    @Override
    public void getListDetail(int orderId) {

    }

    @Override
    public void getListFloor(int orderId) {

    }

    @Override
    public void getListModule(int orderId) {

    }

    @Override
    public void getListScan(int orderId, String floor, String module) {
        localRepository.getListScanPackaging(orderId, floor, module, new HashMap<String, String>())
                .subscribe(new Action1<LogListModulePagkaging>() {
                    @Override
                    public void call(LogListModulePagkaging logListModulePagkaging) {
                        view.showListScan(logListModulePagkaging);
                    }
                });
    }

    @Override
    public void deleteLogScan(int id) {
        localRepository.deleteScanPackaging(id).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_success));
                view.turnOnVibrator();
                view.startMusicSuccess();
            }
        });
    }

    @Override
    public void updateNumberScan(int id, int number) {
        localRepository.updateNumberScanPackaging(id, number).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_update_barcode_success));
                view.turnOnVibrator();
                view.startMusicSuccess();
            }
        });
    }

    @Override
    public void checkBarcode(String barcode, int orderId, String floor, String module) {
        if (barcode.contains(CoreApplication.getInstance().getString(R.string.text_minus))) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_type));
            return;
        }
        if (barcode.length() < 10 || barcode.length() > 13) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_lenght));
            return;
        }

        ProductPackagingEntity product = ListProductPackagingManager.getInstance().getProdctByBarcode(barcode);
        if (product != null) {
            localRepository.findProductPackaging(product.getId()).subscribe(new Action1<ProductPackagingModel>() {
                @Override
                public void call(ProductPackagingModel productPackagingModel) {
                    if (productPackagingModel == null || productPackagingModel.getNumberRest() > 0) {
                        saveBarcode(product, barcode, orderId, floor, module);
                    } else {
                        showError(CoreApplication.getInstance().getString(R.string.text_number_input_had_enough));
                    }
                }
            });

        } else {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_no_exist));
            return;
        }
    }

    void saveBarcode(ProductPackagingEntity product, String barcode, int orderId, String floor, String module) {
        localRepository.saveBarcodeScanPackaging(product, orderId, floor, module, barcode)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                        view.startMusicSuccess();
                        view.turnOnVibrator();
                    }
                });
    }

    public void showError(String error) {
        view.showError(error);
        view.startMusicError();
        view.turnOnVibrator();
    }

}
