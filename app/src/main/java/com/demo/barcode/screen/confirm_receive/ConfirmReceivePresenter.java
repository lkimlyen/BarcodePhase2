package com.demo.barcode.screen.confirm_receive;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.ConfirmInputModel;
import com.demo.architect.data.model.offline.LogScanConfirm;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.ConfirmInputUsecase;
import com.demo.architect.domain.GetInputUnConfirmedUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListDepartmentManager;
import com.demo.barcode.manager.ListOrderConfirmManager;
import com.demo.barcode.manager.ListProductManager;
import com.demo.barcode.manager.ListSOManager;
import com.demo.barcode.manager.UserManager;
import com.demo.barcode.util.ConvertUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmList;
import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class ConfirmReceivePresenter implements ConfirmReceiveContract.Presenter {

    private final String TAG = ConfirmReceivePresenter.class.getName();
    private final ConfirmReceiveContract.View view;
    private final GetListSOUsecase getListSOUsecase;
    private final GetInputUnConfirmedUsecase getInputUnConfirmedUsecase;
    private final ConfirmInputUsecase confirmInputUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    ConfirmReceivePresenter(@NonNull ConfirmReceiveContract.View view, GetListSOUsecase getListSOUsecase,
                            GetInputUnConfirmedUsecase getInputUnConfirmedUsecase, ConfirmInputUsecase confirmInputUsecase) {
        this.view = view;
        this.getListSOUsecase = getListSOUsecase;
        this.getInputUnConfirmedUsecase = getInputUnConfirmedUsecase;
        this.confirmInputUsecase = confirmInputUsecase;
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
    public void getListConfirm(int orderId, int departmentIdOut) {
        view.showProgressBar();
        UserEntity user = UserManager.getInstance().getUser();
        getInputUnConfirmedUsecase.executeIO(new GetInputUnConfirmedUsecase.RequestValue(orderId, user.getRole(), departmentIdOut),
                new BaseUseCase.UseCaseCallback<GetInputUnConfirmedUsecase.ResponseValue,
                        GetInputUnConfirmedUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetInputUnConfirmedUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        // ListOrderConfirmManager.getInstance().setListOrder(successResponse.getEntity());
                        localRepository.addOrderConfirm(successResponse.getEntity()).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_list_confirm_success));
                            }
                        });
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
        view.showListDepartment(ListDepartmentManager.getInstance().getListDepartment(
                UserManager.getInstance().getUser().getRole()
        ));
    }

    @Override
    public void getListConfirmByTimes(int times) {
        localRepository.getListConfirm(times).subscribe(new Action1<RealmList<ConfirmInputModel>>() {
            @Override
            public void call(RealmList<ConfirmInputModel> confirmInputModels) {
                view.showListConfirm(confirmInputModels);
            }
        });
    }

    @Override
    public void checkBarcode(int orderId, String barcode, int departmentId, int times) {
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


        localRepository.findConfirmByBarcode(barcode).subscribe(new Action1<ConfirmInputModel>() {
            @Override
            public void call(ConfirmInputModel confirmInputModel) {
                if (confirmInputModel == null) {
                    showError(CoreApplication.getInstance().getString(R.string.text_barcode_no_exist));
                } else {
                    saveConfirm(confirmInputModel.getId(), confirmInputModel.getDepartmentIDOut(), times);
                }
            }
        });

        if (checkBarcode == 0) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_no_exist));
        }
    }

    @Override
    public void updateNumberConfirm(int logId, int numberScan) {
        localRepository.updateNumnberLogConfirm(logId, numberScan).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_update_barcode_success));
                view.startMusicSuccess();
                view.turnOnVibrator();
                view.hideProgressBar();
            }
        });
    }

    @Override
    public void uploadData() {
        view.showProgressBar();
        localRepository.getListLogScanConfirm().subscribe(new Action1<List<LogScanConfirm>>() {
            @Override
            public void call(List<LogScanConfirm> logScanConfirms) {
                GsonBuilder builder = new GsonBuilder();
                builder.excludeFieldsWithoutExposeAnnotation();
                Gson gson = builder.create();
                String json = gson.toJson(logScanConfirms);
                confirmInputUsecase.executeIO(new ConfirmInputUsecase.RequestValue(UserManager.getInstance().getUser().getRole(),
                        json), new BaseUseCase.UseCaseCallback<ConfirmInputUsecase.ResponseValue,
                        ConfirmInputUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(ConfirmInputUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        localRepository.updateStatusLogConfirm().subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                view.showSuccess(successResponse.getDescription());
                            }
                        });

                    }

                    @Override
                    public void onError(ConfirmInputUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
            }
        });
    }

    public void saveConfirm(int orderProductId, int departmentIdOut, int times) {
        LogScanConfirm logScanConfirm = new LogScanConfirm(orderProductId, departmentIdOut, 1, UserManager.getInstance().getUser().getId(), times,
                ConvertUtils.getDateTimeCurrent());
        localRepository.addLogScanConfirm(logScanConfirm).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                view.startMusicSuccess();
                view.turnOnVibrator();
                view.hideProgressBar();
            }
        });
    }


    public void showError(String error) {
        view.showError(error);
        view.startMusicError();
        view.turnOnVibrator();
    }

}
