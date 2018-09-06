package com.demo.barcode.screen.history_pack;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetApartmentUsecase;
import com.demo.architect.domain.GetCodePackUsecase;
import com.demo.architect.domain.GetListPrintPackageHistoryUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.GetModuleUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListApartmentManager;
import com.demo.barcode.manager.ListCodePackManager;
import com.demo.barcode.manager.ListHistoryManager;
import com.demo.barcode.manager.ListModuleManager;
import com.demo.barcode.manager.ListSOManager;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class HistoryPackagePresenter implements HistoryPackageContract.Presenter {

    private final String TAG = HistoryPackagePresenter.class.getName();
    private final HistoryPackageContract.View view;
    private final GetListSOUsecase getListSOUsecase;
    private final GetListPrintPackageHistoryUsecase getListPrintPackageHistoryUsecase;
    private final GetApartmentUsecase getApartmentUsecase;
    private final GetModuleUsecase getModuleUsecase;
    private final GetCodePackUsecase getCodePackUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    HistoryPackagePresenter(@NonNull HistoryPackageContract.View view, GetListSOUsecase getListSOUsecase, GetListPrintPackageHistoryUsecase getListPrintPackageHistoryUsecase, GetApartmentUsecase getApartmentUsecase, GetModuleUsecase getModuleUsecase, GetCodePackUsecase getCodePackUsecase) {
        this.view = view;

        this.getListSOUsecase = getListSOUsecase;
        this.getListPrintPackageHistoryUsecase = getListPrintPackageHistoryUsecase;
        this.getApartmentUsecase = getApartmentUsecase;
        this.getModuleUsecase = getModuleUsecase;
        this.getCodePackUsecase = getCodePackUsecase;
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
                        //   view.clearDataNoProduct(true);
                    }
                });
    }

    @Override
    public void getListApartment(int orderId) {
        view.showProgressBar();
        getApartmentUsecase.executeIO(new GetApartmentUsecase.RequestValue(orderId),
                new BaseUseCase.UseCaseCallback<GetApartmentUsecase.ResponseValue,
                        GetApartmentUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetApartmentUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListApartmentManager.getInstance().setListDepartment(successResponse.getEntity());
                        view.showListApartment(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetApartmentUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListApartmentManager.getInstance().setListDepartment(new ArrayList<>());

                    }
                });
    }

    @Override
    public void getListModule(int orderId, int orderType, int apartmentId) {
        view.showProgressBar();
        getModuleUsecase.executeIO(new GetModuleUsecase.RequestValue(orderId, orderType, apartmentId),
                new BaseUseCase.UseCaseCallback<GetModuleUsecase.ResponseValue,
                        GetModuleUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetModuleUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        view.showListModule(successResponse.getEntity());
                        ListModuleManager.getInstance().setListModule(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetModuleUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
    }

    @Override
    public void getListCodePack(int orderId, int orderType, int productId) {
        view.showProgressBar();
        getCodePackUsecase.executeIO(new GetCodePackUsecase.RequestValue(orderId, orderType, productId),
                new BaseUseCase.UseCaseCallback<GetCodePackUsecase.ResponseValue,
                        GetCodePackUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetCodePackUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        view.showListCodePack(successResponse.getEntity());
                        ListCodePackManager.getInstance().setListCodePack(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetCodePackUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
    }

    @Override
    public void getListHistory(int orderId, int productId, int apartmentId, String packCode, String sttPack) {
        view.showProgressBar();
        getListPrintPackageHistoryUsecase.executeIO(new GetListPrintPackageHistoryUsecase.RequestValue(orderId, productId, apartmentId,
                        packCode,sttPack),
                new BaseUseCase.UseCaseCallback<GetListPrintPackageHistoryUsecase.ResponseValue,
                        GetListPrintPackageHistoryUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListPrintPackageHistoryUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        if(successResponse.getEntity().size() == 0){
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_data));
                        }else {
                            view.showListHistory(successResponse.getEntity());
                        }

                        ListHistoryManager.getInstance().setListHitory(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetListPrintPackageHistoryUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListHistoryManager.getInstance().setListHitory(new ArrayList<>());
                    }
                });
    }
}
