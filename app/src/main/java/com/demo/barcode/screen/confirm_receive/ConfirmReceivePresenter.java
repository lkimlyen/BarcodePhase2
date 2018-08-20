package com.demo.barcode.screen.confirm_receive;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.UserEntity;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;
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
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_so_success));
                    }

                    @Override
                    public void onError(GetListSOUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListSOManager.getInstance().setListSO(new ArrayList<>());
                        view.clearDataNoProduct(true);
                    }
                });
    }

    @Override
    public void getListTimes(int orderId) {
        SOEntity soEntity = ListSOManager.getInstance().getSOById(orderId);
        view.showListTimes(soEntity.getListTimesOutput());
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
                        view.clearDataNoProduct(false);
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
    public void getListConfirmByTimes(int orderId, int deparmentId, int times) {
        localRepository.getListConfirm(orderId, deparmentId, times).subscribe(new Action1<RealmResults<LogScanConfirm>>() {
            @Override
            public void call(RealmResults<LogScanConfirm> logScanConfirms) {
                view.showListConfirm(logScanConfirms);
            }
        });
    }
    int count = 0;

    @Override
    public int countListConfirmByTimesWaitingUpload(int orderId, int deparmentId, int times) {
        count = 0;
        localRepository.countListConfirmByTimesWaitingUpload(orderId, deparmentId, times).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                count = integer;
            }
        });
        return count;
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

        localRepository.findConfirmByBarcode(orderId, departmentId, times, barcode).subscribe(new Action1<LogScanConfirm>() {
            @Override
            public void call(LogScanConfirm logScanConfirm) {
                if (logScanConfirm == null) {
                    showError(CoreApplication.getInstance().getString(R.string.text_barcode_no_exist));
                } else {
                    if (logScanConfirm.getNumberConfirmed() == logScanConfirm.getNumberScanOut()){
                        showError(CoreApplication.getInstance().getString(R.string.text_number_confirm_residual));
                    }else {
                        saveConfirm(orderId, logScanConfirm.getMasterOutputID(), logScanConfirm.getDepartmentIDOut(), times);
                    }

                }
            }
        });

    }

    @Override
    public void updateNumberConfirm(int orderId, int masterOutputId, int departmentIdOut, int times, int numberScan) {
        localRepository.updateNumnberLogConfirm(orderId, masterOutputId, departmentIdOut, times, numberScan, false).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                view.startMusicSuccess();
                view.turnOnVibrator();
                view.hideProgressBar();
            }
        });
    }

    @Override
    public void uploadData(int orderId, int departmentIdOut, int times) {
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
                                getListConfirmByTimes(orderId, departmentIdOut, times);
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

    public void saveConfirm(int orderId, int marterOutputId, int departmentIdOut, int times) {
        localRepository.updateNumnberLogConfirm(orderId, marterOutputId, departmentIdOut, times, 1, true).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_update_barcode_success));
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
