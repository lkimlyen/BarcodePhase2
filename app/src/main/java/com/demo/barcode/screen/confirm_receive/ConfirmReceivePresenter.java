package com.demo.barcode.screen.confirm_receive;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetInputUnConfirmedUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.barcode.manager.ListDepartmentManager;
import com.demo.barcode.manager.ListOrderConfirmManager;
import com.demo.barcode.manager.ListSOManager;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class ConfirmReceivePresenter implements ConfirmReceiveContract.Presenter {

    private final String TAG = ConfirmReceivePresenter.class.getName();
    private final ConfirmReceiveContract.View view;
    private final GetListSOUsecase getListSOUsecase;
    private final GetInputUnConfirmedUsecase getInputUnConfirmedUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    ConfirmReceivePresenter(@NonNull ConfirmReceiveContract.View view, GetListSOUsecase getListSOUsecase,
                            GetInputUnConfirmedUsecase getInputUnConfirmedUsecase) {
        this.view = view;
        this.getListSOUsecase = getListSOUsecase;
        this.getInputUnConfirmedUsecase = getInputUnConfirmedUsecase;
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
        view.showProgressBar();
        getListSOUsecase.executeIO(new GetListSOUsecase.RequestValue(1),
                new BaseUseCase.UseCaseCallback<GetListSOUsecase.ResponseValue,
                        GetListSOUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListSOUsecase.ResponseValue successResponse) {
                        view.showListSO(successResponse.getEntity());
                        ListSOManager.getInstance().setListSO(successResponse.getEntity());
                        view.hideProgressBar();
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
    public void getListTimes(int orderId) {
        SOEntity soEntity = ListSOManager.getInstance().getSOById(orderId);
        view.showListTimes(soEntity.getListTimesInput());
    }

    @Override
    public void getListConfirm(int orderId, int departmentIdIn, int departmentIdOut) {
        view.showProgressBar();
        getInputUnConfirmedUsecase.executeIO(new GetInputUnConfirmedUsecase.RequestValue(orderId, departmentIdIn, departmentIdOut),
                new BaseUseCase.UseCaseCallback<GetInputUnConfirmedUsecase.ResponseValue,
                        GetInputUnConfirmedUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetInputUnConfirmedUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListOrderConfirmManager.getInstance().setListOrder(successResponse.getEntity());
                        view.showListConfirm(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetInputUnConfirmedUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListOrderConfirmManager.getInstance().setListOrder(new ArrayList<>());
                    }
                });
    }

    @Override
    public void getListDepartment() {
        view.showListDepartment(ListDepartmentManager.getInstance().getListDepartment());
    }
}
