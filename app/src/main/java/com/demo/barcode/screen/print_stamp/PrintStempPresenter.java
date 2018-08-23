package com.demo.barcode.screen.print_stamp;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.offline.LogListModulePagkaging;
import com.demo.architect.data.model.offline.LogListOrderPackaging;
import com.demo.architect.data.repository.base.local.LocalRepository;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class PrintStempPresenter implements PrintStempContract.Presenter {

    private final String TAG = PrintStempPresenter.class.getName();
    private final PrintStempContract.View view;
    @Inject
    LocalRepository localRepository;

    @Inject
    PrintStempPresenter(@NonNull PrintStempContract.View view) {
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
    public void getListSerialPack(int orderId, String floor, String module) {
        localRepository.getListScanPackaging(orderId, floor, module)
                .subscribe(new Action1<LogListModulePagkaging>() {
                    @Override
                    public void call(LogListModulePagkaging logListModulePagkaging) {
                        view.showListSerialPack(logListModulePagkaging);
                    }
                });
    }

    @Override
    public void getOrderPackaging(int orderId) {
        localRepository.findOrderPackaging(orderId).subscribe(new Action1<LogListOrderPackaging>() {
            @Override
            public void call(LogListOrderPackaging logListOrderPackaging) {
                view.showOrderPackaging(logListOrderPackaging);
            }
        });
    }

    @Override
    public void getTotalScanBySerialPack(int logId) {
        localRepository.getTotalScanBySerialPack(logId).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                view.showTotalNumberScan(integer);
            }
        });
    }
}
