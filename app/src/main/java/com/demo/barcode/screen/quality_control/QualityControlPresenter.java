package com.demo.barcode.screen.quality_control;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.offline.QualityControlModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetInputForProductDetailUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListDepartmentManager;
import com.demo.barcode.manager.ListProductManager;
import com.demo.barcode.manager.ListSOManager;
import com.demo.barcode.manager.UserManager;

import java.util.ArrayList;

import javax.inject.Inject;

import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class QualityControlPresenter implements QualityControlContract.Presenter {

    private final String TAG = QualityControlPresenter.class.getName();
    private final QualityControlContract.View view;
    private final GetListSOUsecase getListSOUsecase;
    private final GetInputForProductDetailUsecase getInputForProductDetail;
    @Inject
    LocalRepository localRepository;

    @Inject
    QualityControlPresenter(@NonNull QualityControlContract.View view, GetListSOUsecase getListSOUsecase, GetInputForProductDetailUsecase getInputForProductDetailUsecase) {
        this.view = view;
        this.getListSOUsecase = getListSOUsecase;
        this.getInputForProductDetail = getInputForProductDetailUsecase;
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
    public void getListDepartment() {
        view.showListDepartment(ListDepartmentManager.getInstance().getListDepartment(
                UserManager.getInstance().getUser().getRole()
        ));
    }

    @Override
    public void getListSO(int orderType) {
        view.showProgressBar();
        getListSOUsecase.executeIO(new GetListSOUsecase.RequestValue(orderType),
                new BaseUseCase.UseCaseCallback<GetListSOUsecase.ResponseValue,
                        GetListSOUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListSOUsecase.ResponseValue successResponse) {
                        view.showListSO(successResponse.getEntity());
                        ListSOManager.getInstance().setListSO(successResponse.getEntity());
                        view.hideProgressBar();
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_so_success));
                    }

                    @Override
                    public void onError(GetListSOUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListSOManager.getInstance().setListSO(new ArrayList<>());
                    }
                });
    }

    @Override
    public void getListProduct(int orderId) {
        view.showProgressBar();
        getInputForProductDetail.executeIO(new GetInputForProductDetailUsecase.RequestValue(orderId, UserManager.getInstance().getUser().getRole()),
                new BaseUseCase.UseCaseCallback<GetInputForProductDetailUsecase.ResponseValue,
                        GetInputForProductDetailUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetInputForProductDetailUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListProductManager.getInstance().setListProduct(successResponse.getEntity());
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_list_detail_success));
                    }

                    @Override
                    public void onError(GetInputForProductDetailUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListProductManager.getInstance().setListProduct(new ArrayList<>());
                    }
                });
    }

    @Override
    public void checkBarcode(String barcode, int departmentId, int times) {

    }

    @Override
    public void getListQualityControl(int orderId, int departmentId) {
        localRepository.getListQualityControl(orderId,departmentId).subscribe(new Action1<RealmResults<QualityControlModel>>() {
            @Override
            public void call(RealmResults<QualityControlModel> qualityControlModels) {
                view.showListQualityControl(qualityControlModels);
            }
        });
    }

    @Override
    public void removeItemQualityControl(int id) {

    }


}
