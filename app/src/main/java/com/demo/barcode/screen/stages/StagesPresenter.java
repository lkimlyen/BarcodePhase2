package com.demo.barcode.screen.stages;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.NumberInput;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.NumberInputModel;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetInputForProductDetail;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.ScanProductDetailOutUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListDepartmentManager;
import com.demo.barcode.manager.ListProductManager;
import com.demo.barcode.manager.UserManager;
import com.demo.barcode.util.ConvertUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

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
    private final ScanProductDetailOutUsecase scanProductDetailOutUsecase;

    @Inject
    LocalRepository localRepository;

    @Inject
    StagesPresenter(@NonNull StagesContract.View view,
                    GetInputForProductDetail getInputForProductDetail, GetListSOUsecase getListSOUsecase, ScanProductDetailOutUsecase scanProductDetailOutUsecase) {
        this.view = view;
        this.getInputForProductDetail = getInputForProductDetail;
        this.getListSOUsecase = getListSOUsecase;
        this.scanProductDetailOutUsecase = scanProductDetailOutUsecase;
    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void start() {
        Log.d(TAG, TAG + ".start() called");
        getListSO();
        getListDepartment();
    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    @Override
    public void checkBarcode(String barcode, int departmentId) {
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
                    localRepository.getProductDetail(model).subscribe(new Action1<ProductDetail>() {
                        @Override
                        public void call(ProductDetail productDetail) {
                            if (productDetail.getListInput().size() > 1) {
                                List<NumberInputModel> listTimes = new ArrayList<>();
                                for (int i = 0; i < productDetail.getListInput().size(); i++) {
                                    NumberInputModel input = productDetail.getListInput().get(i);
                                    if (input.getNumberRest() > 1) {
                                        listTimes.add(input);
                                    }
                                }
                                if (listTimes.size() > 1) {
                                    view.showChooseTimes(listTimes, model, barcode);
                                } else {
                                    for (NumberInputModel numberInput : listTimes) {
                                        saveBarcode(numberInput, model, barcode, departmentId);
                                    }
                                }
                            } else {
                                NumberInputModel numberInput = productDetail.getListInput().get(0);
                                saveBarcode(numberInput, model, barcode, departmentId);
                            }

                        }
                    });
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
    public void saveBarcode(NumberInputModel numberInput, ProductEntity productEntity, String barcode, int departmentId) {

        if (numberInput.getNumberRest() + numberInput.getNumberScanned() == numberInput.getNumberTotal()) {
            saveBarcodeToDataBase(numberInput, productEntity, barcode, departmentId);
        } else {
            view.showCheckResidual(numberInput.getTimes());
            view.startMusicError();
            view.turnOnVibrator();
        }
    }

    @Override
    public void uploadData(int orderId) {
        view.showProgressBar();
        localRepository.getListLogScanStagesUpdate(orderId).subscribe(new Action1<List<LogScanStages>>() {
            @Override
            public void call(List<LogScanStages> logScanStages) {
                GsonBuilder builder = new GsonBuilder();
                builder.excludeFieldsWithoutExposeAnnotation();
                Gson gson = builder.create();
                scanProductDetailOutUsecase.executeIO(new ScanProductDetailOutUsecase.RequestValue(gson.toJson(logScanStages)),
                        new BaseUseCase.UseCaseCallback<ScanProductDetailOutUsecase.ResponseValue,
                                ScanProductDetailOutUsecase.ErrorValue>() {
                            @Override
                            public void onSuccess(ScanProductDetailOutUsecase.ResponseValue successResponse) {
                                view.hideProgressBar();
                                view.showSuccess(successResponse.getDescription());
                            }

                            @Override
                            public void onError(ScanProductDetailOutUsecase.ErrorValue errorResponse) {
                                view.hideProgressBar();
                                view.showError(errorResponse.getDescription());
                            }
                        });
            }
        });

    }

    @Override
    public void deleteScanStages(int stagesId) {
        view.showProgressBar();
        localRepository.deleteScanStages(stagesId).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_success));
            }
        });
    }

    @Override
    public void updateNumberScanStages(int stagesId, int numberInput) {
        view.showProgressBar();
        localRepository.updateNumberScanStages(stagesId, numberInput).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                view.startMusicSuccess();
                view.turnOnVibrator();
                view.hideProgressBar();
            }
        });
    }

    private boolean listStagesIsNull = false;

    @Override
    public void getListScanStages(int orderId, int departmentId) {
        localRepository.getListScanStagseByDepartment(orderId, departmentId, UserManager.getInstance().getUser().getUserId())
                .subscribe(new Action1<LogListScanStages>() {
                    @Override
                    public void call(LogListScanStages logListScanStages) {
                        if (logListScanStages != null) {
                            view.showListLogScanStages(logListScanStages);
                            listStagesIsNull = false;
                        } else {
                            listStagesIsNull = true;
                        }
                    }
                });
    }

    public void saveBarcodeToDataBase(NumberInputModel numberInput, ProductEntity productEntity, String barcode, int departmentId) {
        view.showProgressBar();
        int userId = UserManager.getInstance().getUser().getUserId();

        LogScanStages logScanStages = new LogScanStages(productEntity.getOrderId(), userId, departmentId, productEntity.getProductDetailID(),
                barcode, productEntity.getModule(), 1, numberInput.getTimes(), ConvertUtils.getDateTimeCurrent(), userId);
        localRepository.addLogScanStagesAsync(logScanStages, productEntity).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if (listStagesIsNull) {
                    getListScanStages(productEntity.getOrderId(), departmentId);
                }
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                view.startMusicSuccess();
                view.turnOnVibrator();
                view.hideProgressBar();
            }
        });

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
