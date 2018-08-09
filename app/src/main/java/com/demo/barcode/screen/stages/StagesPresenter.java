package com.demo.barcode.screen.stages;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.NumberInput;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetInputForProductDetail;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListDepartmentManager;
import com.demo.barcode.manager.ListProductManager;
import com.demo.barcode.manager.UserManager;
import com.demo.barcode.util.ConvertUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class StagesPresenter implements StagesContract.Presenter {

    private final String TAG = StagesPresenter.class.getName();
    private final StagesContract.View view;
    private final GetInputForProductDetail getInputForProductDetail;
    private final GetListSOUsecase getListSOUsecase;

    @Inject
    LocalRepository localRepository;

    @Inject
    StagesPresenter(@NonNull StagesContract.View view,
                    GetInputForProductDetail getInputForProductDetail, GetListSOUsecase getListSOUsecase) {
        this.view = view;
        this.getInputForProductDetail = getInputForProductDetail;
        this.getListSOUsecase = getListSOUsecase;
    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void start() {
        Log.d(TAG, TAG + ".start() called");
        getListDepartment();
    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    @Override
    public void checkBarcode(String barcode, int orderId, int departmentId, double latitude, double longitude) {
        if (barcode.contains(CoreApplication.getInstance().getString(R.string.text_minus))) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_type));
            return;
        }
        if (barcode.length() < 10 || barcode.length() > 13) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_lenght));
            return;
        }

        List<ProductEntity> list = ListProductManager.getInstance().getListProduct();


        if (list.size() == 0) {
            showError(CoreApplication.getInstance().getString(R.string.text_product_empty));

            return;
        }

        int checkBarcode = 0;

        for (ProductEntity model : list) {
            if (model.getBarcode().equals(barcode)) {
                checkBarcode++;
                if (model.getListDepartmentID().contains(departmentId)) {
                    if (model.getListInput().size() > 1) {
                        List<NumberInput> listTimes = new ArrayList<>();
                        for (int i = 0; i < model.getListInput().size(); i++) {
                            NumberInput input = model.getListInput().get(i);
                            if (input.getNumberWaitting() > 1) {
                                listTimes.add(input);
                            }
                        }
                        if (listTimes.size() > 1) {
                            view.showChooseTimes(listTimes);
                        } else {
                            for (NumberInput numberInput : listTimes) {
                                saveBarcode(numberInput, barcode, orderId, departmentId, latitude, longitude);
                            }
                        }

                    } else {
                        NumberInput numberInput = model.getListInput().get(0);
                        saveBarcode(numberInput, barcode, orderId, departmentId, latitude, longitude);
                    }


                } else {
                    showError(CoreApplication.getInstance().getString(R.string.text_product_not_in_stages));
                }

                return;
            }
        }

        if (checkBarcode == 0) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_no_exist));
        }
    }

    public void showError(String error) {
        view.showError(error);
        view.startMusicError();
        view.turnOnVibrator();
    }

    private int count = 0;

    @Override
    public int countLogScanStages(int orderId, int departmentId) {
        localRepository.countLogScanStages(orderId, departmentId).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                count = integer;
            }
        });
        return count;
    }

    @Override
    public void saveBarcode(NumberInput numberInput, String barcode, int orderId, int departmentId, double latitude, double longitude) {

        if (numberInput.getNumberWaitting() + numberInput.getNumberSuccess() == numberInput.getNumberTotalInput()) {
            saveBarcodeToDataBase(numberInput, barcode, orderId, departmentId, latitude, longitude);
        } else {
            view.showCheckResidual(numberInput.getTimesInput());
            view.startMusicError();
            view.turnOnVibrator();
        }
    }

    public void saveBarcodeToDataBase(NumberInput numberInput, String barcode, int orderId, int departmentId, double latitude, double longitude) {
        view.showProgressBar();
        String deviceTime = ConvertUtils.getDateTimeCurrent();
        int userId = UserManager.getInstance().getUser().getUserId();
        String phone = Settings.Secure.getString(CoreApplication.getInstance().getContentResolver(),
                Settings.Secure.ANDROID_ID);


//        localRepository.addLogScanCreatePack(productModel, orderModel, model, orderModel.getId(), barcode).subscribe(new Action1<String>() {
//            @Override
//            public void call(String String) {
//                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
//                view.startMusicSuccess();
//                view.turnOnVibrator();
//                product.setNumScaned(product.getNumScaned() + 1);
//                if (product.getNumber() - product.getNumScaned() == 0) {
//                    product.setFull(true);
//                }
//                ListProductManager.getInstance().updateEntity(product);
//
//                view.hideProgressBar();
//            }
//        });


    }


    @Override
    public void getListDepartment() {
        view.showListDepartment(ListDepartmentManager.getInstance().getListDepartment());
    }

    @Override
    public void getListSO() {
        view.showProgressBar();
        getListSOUsecase.executeIO(new GetListSOUsecase.RequestValue(1),
                new BaseUseCase.UseCaseCallback<GetListSOUsecase.ResponseValue,
                        GetListSOUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListSOUsecase.ResponseValue successResponse) {
                        view.showListSO(successResponse.getEntity());
                        view.hideProgressBar();
                    }

                    @Override
                    public void onError(GetListSOUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
    }

    @Override
    public void getListProduct(int orderId) {
        view.showProgressBar();
        getInputForProductDetail.executeIO(new GetInputForProductDetail.RequestValue(orderId, UserManager.getInstance().getUser().getUserId()),
                new BaseUseCase.UseCaseCallback<GetInputForProductDetail.ResponseValue,
                        GetInputForProductDetail.ErrorValue>() {
                    @Override
                    public void onSuccess(GetInputForProductDetail.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListProductManager.getInstance().setListProduct(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetInputForProductDetail.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListProductManager.getInstance().setListProduct(new ArrayList<>());
                    }
                });

    }
}
