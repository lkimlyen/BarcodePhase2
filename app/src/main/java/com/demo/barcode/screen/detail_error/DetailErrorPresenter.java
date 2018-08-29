package com.demo.barcode.screen.detail_error;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetListReasonUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListReasonManager;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class DetailErrorPresenter implements DetailErrorContract.Presenter {

    private final String TAG = DetailErrorPresenter.class.getName();
    private final DetailErrorContract.View view;
    private final GetListReasonUsecase getListReasonUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    DetailErrorPresenter(@NonNull DetailErrorContract.View view, GetListReasonUsecase getListReasonUsecase) {
        this.view = view;
        this.getListReasonUsecase = getListReasonUsecase;
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
    public void addImage(String pathFile) {
        view.showProgressBar();
        localRepository.addImageModel(pathFile).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.hideProgressBar();
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_add_success));
            }
        });
    }

    @Override
    public void deleteImage(int id) {
        view.showProgressBar();
        localRepository.deleteImageModel(id).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.hideProgressBar();
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_image_success));
            }
        });
    }

    @Override
    public void getListReason() {
        view.showProgressBar();
        getListReasonUsecase.executeIO(new GetListReasonUsecase.RequestValue(),
                new BaseUseCase.UseCaseCallback<GetListReasonUsecase.ResponseValue,
                        GetListReasonUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListReasonUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        view.showListReason(successResponse.getEntity());
                      //  ListReasonManager.getInstance().setListReason(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetListReasonUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
    }
}
