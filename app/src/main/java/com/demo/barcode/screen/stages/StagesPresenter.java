package com.demo.barcode.screen.stages;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.NumberInputModel;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetInputForProductDetailUsecase;
import com.demo.architect.domain.GetListProductDetailGroupUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.GetTimesInputAndOutputByDepartmentUsecase;
import com.demo.architect.domain.ScanProductDetailOutUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListDepartmentManager;
import com.demo.barcode.manager.ListProductGroupManager;
import com.demo.barcode.manager.ListProductManager;
import com.demo.barcode.manager.ListSOManager;
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
    private final GetInputForProductDetailUsecase getInputForProductDetail;
    private final GetListSOUsecase getListSOUsecase;
    private final ScanProductDetailOutUsecase scanProductDetailOutUsecase;
    private final GetListProductDetailGroupUsecase getListProductDetailGroupUsecase;
    private final GetTimesInputAndOutputByDepartmentUsecase getTimesInputAndOutputByDepartmentUsecase;

    @Inject
    LocalRepository localRepository;

    @Inject
    StagesPresenter(@NonNull StagesContract.View view,
                    GetInputForProductDetailUsecase getInputForProductDetail, GetListSOUsecase getListSOUsecase, ScanProductDetailOutUsecase scanProductDetailOutUsecase, GetListProductDetailGroupUsecase getListProductDetailGroupUsecase, GetTimesInputAndOutputByDepartmentUsecase getTimesInputAndOutputByDepartmentUsecase) {
        this.view = view;
        this.getInputForProductDetail = getInputForProductDetail;
        this.getListSOUsecase = getListSOUsecase;
        this.scanProductDetailOutUsecase = scanProductDetailOutUsecase;
        this.getListProductDetailGroupUsecase = getListProductDetailGroupUsecase;
        this.getTimesInputAndOutputByDepartmentUsecase = getTimesInputAndOutputByDepartmentUsecase;
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

    private boolean allowedToSave = true;

    @Override
    public void checkBarcode(String barcode, int departmentId, int times, boolean groupCode) {
        if (barcode.contains(CoreApplication.getInstance().getString(R.string.text_minus))) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_type));
            return;
        }

        List<ProductEntity> list = ListProductManager.getInstance().getListProduct();

        if (list.size() == 0) {
            showError(CoreApplication.getInstance().getString(R.string.text_product_empty));

            return;
        }

        ProductEntity model = ListProductManager.getInstance().getProductByBarcode(barcode);
        if (model != null) {
            if (!groupCode) {
                if (model.getListDepartmentID().contains(departmentId)) {
                    localRepository.getProductDetail(model).subscribe(new Action1<ProductDetail>() {
                        @Override
                        public void call(ProductDetail productDetail) {
                            NumberInputModel numberInput = null;
                            for (int i = 0; i < productDetail.getListInput().size(); i++) {
                                NumberInputModel input = productDetail.getListInput().get(i);
                                if (input.getTimes() == times) {
                                    numberInput = input;
                                    break;
                                }
                            }
                            if (numberInput != null) {
                                if (numberInput.getNumberRest() > 0) {

                                    saveBarcodeToDataBase(times, model, 1, departmentId);

                                } else {
                                    view.showCheckResidual(times, model, departmentId);
                                    view.startMusicError();
                                    view.turnOnVibrator();
                                }
                            } else {
                                showError(CoreApplication.getInstance().getString(R.string.text_product_not_in_times));
                            }
                        }

                    });

                } else {
                    showError(CoreApplication.getInstance().getString(R.string.text_product_not_in_stages));
                }
            } else {
                ProductGroupEntity groupEntity = ListProductGroupManager.getInstance().getProductById(model.getProductDetailID());
                if (groupEntity == null) {
                    showError(CoreApplication.getInstance().getString(R.string.text_product_not_in_group));
                    return;
                }

                List<ProductGroupEntity> productGroupList = ListProductGroupManager.getInstance().getListProductByGroupCode(groupEntity.getGroupCode());

                boolean existDepartment = false;
                for (ProductGroupEntity item : productGroupList) {
                    ProductEntity productEntity = ListProductManager.getInstance().getProductById(item.getProductDetailID());
                    if (productEntity.getListDepartmentID().contains(departmentId)) {
                        existDepartment = true;
                        break;
                    }
                }
                allowedToSave = true;
                if (existDepartment) {
                    for (ProductGroupEntity item : productGroupList) {
                        ProductEntity productEntity = ListProductManager.getInstance().getProductById(item.getProductDetailID());
                        localRepository.getProductDetail(productEntity).subscribe(new Action1<ProductDetail>() {
                            @Override
                            public void call(ProductDetail productDetail) {
                                NumberInputModel numberInput = null;
                                for (int i = 0; i < productDetail.getListInput().size(); i++) {
                                    NumberInputModel input = productDetail.getListInput().get(i);
                                    if (input.getTimes() == times) {
                                        numberInput = input;
                                        break;
                                    }
                                }
                                if (numberInput != null) {
                                    if (numberInput.getNumberRest() > 0 && numberInput.getNumberRest() == item.getNumber()) {
                                        allowedToSave = true;
                                    } else {
                                        allowedToSave = false;
                                        view.showCheckResidualInGroup(times, productGroupList, departmentId);
                                    }
                                } else {
                                    showError(CoreApplication.getInstance().getString(R.string.text_product_not_in_times));
                                }
                            }

                        });

                        if (!allowedToSave) {
                            return;
                        }
                    }

                    saveListWithGroupCode(times, productGroupList, departmentId);
                } else {
                    showError(CoreApplication.getInstance().getString(R.string.no_product_in_group_to_department));
                }
                //  saveListWithGroupCode(numberInput, groupEntity, barcode, departmentId);

            }
        } else {
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
    public int countLogScanStages(int orderId, int departmentId, int times) {
        localRepository.countLogScanStages(orderId, departmentId, times).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                count = integer;
            }
        });
        return count;
    }


    @Override
    public void uploadData(int orderId, int departmentId, int times) {
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
                                localRepository.updateStatusScanStagesByOrder(orderId).subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.showSuccess(successResponse.getDescription());
                                        getListScanStages(orderId, departmentId, times);
                                    }
                                });
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
                view.hideProgressBar();
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_success));
            }
        });
    }

    @Override
    public void updateNumberScanStages(int stagesId, int numberInput, boolean update) {
        view.showProgressBar();
        localRepository.updateNumberScanStages(stagesId, numberInput).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if (update) {
                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                    view.startMusicSuccess();
                    view.turnOnVibrator();
                }

                view.hideProgressBar();

            }
        });
    }

    @Override
    public void getListScanStages(int orderId, int departmentId, int times) {
        localRepository.getListScanStagseByDepartment(orderId, departmentId, UserManager.getInstance().getUser().getId(), times)
                .subscribe(new Action1<LogListScanStages>() {
                    @Override
                    public void call(LogListScanStages logListScanStages) {
                        view.showListLogScanStages(logListScanStages);
                        //view.showGroupCode(logListScanStages.getListGroupCodes());
                    }
                });
    }

    @Override
    public void getListTimes(int orderId, int departmentId) {
        view.showProgressBar();
        getTimesInputAndOutputByDepartmentUsecase.executeIO(new GetTimesInputAndOutputByDepartmentUsecase.RequestValue(orderId, departmentId),
                new BaseUseCase.UseCaseCallback<GetTimesInputAndOutputByDepartmentUsecase.ResponseValue, GetTimesInputAndOutputByDepartmentUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetTimesInputAndOutputByDepartmentUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        view.showListTimes(successResponse.getEntity().getListTimesOutput());
                    }

                    @Override
                    public void onError(GetTimesInputAndOutputByDepartmentUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
    }

    @Override
    public void uploadDataAll(int orderId, int departmentId, int times) {
        view.showProgressBar();
        localRepository.getListLogScanStagesUpdate().subscribe(new Action1<List<LogScanStages>>() {
            @Override
            public void call(List<LogScanStages> logScanStages) {
                if (logScanStages.size() == 0) {
                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_no_data_upload));
                }
                GsonBuilder builder = new GsonBuilder();
                builder.excludeFieldsWithoutExposeAnnotation();
                Gson gson = builder.create();
                scanProductDetailOutUsecase.executeIO(new ScanProductDetailOutUsecase.RequestValue(gson.toJson(logScanStages)),
                        new BaseUseCase.UseCaseCallback<ScanProductDetailOutUsecase.ResponseValue,
                                ScanProductDetailOutUsecase.ErrorValue>() {
                            @Override
                            public void onSuccess(ScanProductDetailOutUsecase.ResponseValue successResponse) {
                                view.hideProgressBar();
                                getListProduct(orderId, true);
                                localRepository.updateStatusScanStages().subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.showSuccess(successResponse.getDescription());
                                        getListScanStages(orderId, departmentId, times);
                                    }
                                });
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
    public void saveBarcodeToDataBase(int times, ProductEntity
            productEntity, int number, int departmentId) {
        view.showProgressBar();
        UserEntity user = UserManager.getInstance().getUser();

        LogScanStages logScanStages = new LogScanStages(productEntity.getOrderId(), departmentId, user.getRole(), productEntity.getProductDetailID(),
                productEntity.getBarcode(), productEntity.getModule(), number, times, ConvertUtils.getDateTimeCurrent(), user.getId(), number);
        localRepository.addLogScanStagesAsync(logScanStages, productEntity).subscribe(new Action1<String>() {
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
    public void getListGroupCode(int orderId) {
        view.showProgressBar();
        getListProductDetailGroupUsecase.executeIO(new GetListProductDetailGroupUsecase.RequestValue(orderId),
                new BaseUseCase.UseCaseCallback<GetListProductDetailGroupUsecase.ResponseValue,
                        GetListProductDetailGroupUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListProductDetailGroupUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListProductGroupManager.getInstance().setListProduct(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetListProductDetailGroupUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        ListProductGroupManager.getInstance().setListProduct(new ArrayList<>());
                        //  view.showError(errorResponse.getDescription());
                    }
                });
    }

    @Override
    public void saveListWithGroupCode(int times, List<ProductGroupEntity> list, int departmentId) {

        for (ProductGroupEntity item : list) {
            final ProductEntity productEntity = ListProductManager.getInstance().getProductById(item.getProductDetailID());
            saveBarcodeToDataBase(times, productEntity, item.getNumber(), departmentId);

        }
    }

    @Override
    public void saveListWithGroupCodeEnough(int times, List<ProductGroupEntity> list, int departmentId) {
        for (ProductGroupEntity item : list) {
            final ProductEntity productEntity = ListProductManager.getInstance().getProductById(item.getProductDetailID());
            localRepository.getProductDetail(productEntity).subscribe(new Action1<ProductDetail>() {
                @Override
                public void call(ProductDetail productDetail) {
                    NumberInputModel numberInput = null;
                    for (int i = 0; i < productDetail.getListInput().size(); i++) {
                        NumberInputModel input = productDetail.getListInput().get(i);
                        if (input.getTimes() == times) {
                            numberInput = input;
                            break;
                        }
                    }
                    if (numberInput.getNumberRest() < item.getNumber()) {
                        saveBarcodeToDataBase(times, productEntity, numberInput.getNumberRest(), departmentId);
                    } else {
                        saveBarcodeToDataBase(times, productEntity, item.getNumber(), departmentId);
                    }


                }

            });

        }
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
                        view.clearDataNoProduct(true);
                    }
                });
    }

    @Override
    public void getListProduct(int orderId, boolean refresh) {
        view.showProgressBar();
        UserEntity user = UserManager.getInstance().getUser();
        getInputForProductDetail.executeIO(new GetInputForProductDetailUsecase.RequestValue(orderId, UserManager.getInstance().getUser().getRole()),
                new BaseUseCase.UseCaseCallback<GetInputForProductDetailUsecase.ResponseValue,
                        GetInputForProductDetailUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetInputForProductDetailUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListProductManager.getInstance().setListProduct(successResponse.getEntity());
                        if (!refresh) {
                            view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_list_detail_success));
                        }

                        getListTimes(orderId, user.getRole());
                    }

                    @Override
                    public void onError(GetInputForProductDetailUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListProductManager.getInstance().setListProduct(new ArrayList<>());
                        view.clearDataNoProduct(false);
                    }
                });

    }

}
