package com.demo.barcode.screen.detail_error;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.offline.QualityControlModel;
import com.demo.architect.data.model.offline.QualityControlWindowModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetListReasonUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListReasonManager;
import com.demo.barcode.manager.UserManager;

import java.util.Collection;

import javax.inject.Inject;

import io.realm.RealmList;
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
    public void addImage(long id, String pathFile) {
        view.showProgressBar();
        localRepository.addImageModel(id, pathFile, UserManager.getInstance().getUser().getOrderType()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.hideProgressBar();
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_add_success));
            }
        });
    }

    @Override
    public void deleteImage(long id) {
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
    public void getListReason(long id) {
        view.showProgressBar();
        getListReasonUsecase.executeIO(new GetListReasonUsecase.RequestValue(),
                new BaseUseCase.UseCaseCallback<GetListReasonUsecase.ResponseValue,
                        GetListReasonUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListReasonUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        view.showListReason(successResponse.getEntity().getList());
                        localRepository.getListReasonQualityControl(id,UserManager.getInstance().getUser().getOrderType()).subscribe(new Action1<RealmList<Integer>>() {
                            @Override
                            public void call(RealmList<Integer> integerRealmList) {
                                view.showUpdateListCounterSelect(integerRealmList);
                            }
                        });
                        //  ListReasonManager.getInstance().setListReason(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetListReasonUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
    }

    @Override
    public void getDetailQualityControl(long id) {
        if (UserManager.getInstance().getUser().getOrderType() == 4) {
            localRepository.getDetailQualityControlWindow(id).subscribe(new Action1<QualityControlWindowModel>() {
                @Override
                public void call(QualityControlWindowModel qualityControlModel) {
                    view.showDetailQualityControlƯindow(qualityControlModel);
                    view.showImageError(qualityControlModel.getImageList());
                }
            });
        } else {
            localRepository.getDetailQualityControl(id).subscribe(new Action1<QualityControlModel>() {
                @Override
                public void call(QualityControlModel qualityControlModel) {
                    view.showDetailQualityControl(qualityControlModel);
                    view.showImageError(qualityControlModel.getImageList());
                }
            });
        }
    }

    @Override
    public void save(long id, int numberFailed, String description, Collection<Integer> idList) {
        if (UserManager.getInstance().getUser().getOrderType() == 4) {
            localRepository.updateDetailErrorQCWindow(id, numberFailed, description, idList).subscribe(new Action1<String>() {
                @Override
                public void call(String s) {
                    view.goBackQualityControl();
                }
            });
        }else {
            localRepository.updateDetailErrorQC(id, numberFailed, description, idList).subscribe(new Action1<String>() {
                @Override
                public void call(String s) {
                    view.goBackQualityControl();
                }
            });
        }


    }

}
