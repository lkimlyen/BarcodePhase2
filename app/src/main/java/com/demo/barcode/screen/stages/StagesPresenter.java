package com.demo.barcode.screen.stages;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.GroupScan;
import com.demo.architect.data.model.offline.LogListScanStages;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.NumberInputModel;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.CheckUpdateForGroupUsecase;
import com.demo.architect.domain.GetInputForProductDetailUsecase;
import com.demo.architect.domain.GetListProductDetailGroupUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.GetTimesInputAndOutputByDepartmentUsecase;
import com.demo.architect.domain.ScanProductDetailOutUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListDepartmentManager;
import com.demo.barcode.manager.ListGroupManager;
import com.demo.barcode.manager.ListProductManager;
import com.demo.barcode.manager.ListSOManager;
import com.demo.barcode.manager.UserManager;
import com.demo.barcode.util.ConvertUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private final CheckUpdateForGroupUsecase checkUpdateForGroupUsecase;

    @Inject
    LocalRepository localRepository;

    @Inject
    StagesPresenter(@NonNull StagesContract.View view,
                    GetInputForProductDetailUsecase getInputForProductDetail, GetListSOUsecase getListSOUsecase, ScanProductDetailOutUsecase scanProductDetailOutUsecase, GetListProductDetailGroupUsecase getListProductDetailGroupUsecase, GetTimesInputAndOutputByDepartmentUsecase getTimesInputAndOutputByDepartmentUsecase, CheckUpdateForGroupUsecase checkUpdateForGroupUsecase) {
        this.view = view;
        this.getInputForProductDetail = getInputForProductDetail;
        this.getListSOUsecase = getListSOUsecase;
        this.scanProductDetailOutUsecase = scanProductDetailOutUsecase;
        this.getListProductDetailGroupUsecase = getListProductDetailGroupUsecase;
        this.getTimesInputAndOutputByDepartmentUsecase = getTimesInputAndOutputByDepartmentUsecase;
        this.checkUpdateForGroupUsecase = checkUpdateForGroupUsecase;
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

                                    saveBarcodeToDataBase(times, model, 1, departmentId, null, true);

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
                ProductGroupEntity groupEntity = ListGroupManager.getInstance().getProductById(model.getProductDetailID());
                if (groupEntity == null) {
                    showError(CoreApplication.getInstance().getString(R.string.text_product_not_in_group));
                    return;
                }

                GroupEntity groupEntityList = ListGroupManager.getInstance().getGroupEntityByGroupCode(groupEntity.getGroupCode());

                boolean existDepartment = false;
                for (ProductGroupEntity item : groupEntityList.getProducGroupList()) {
                    ProductEntity productEntity = ListProductManager.getInstance().getProductById(item.getProductDetailID());
                    if (productEntity != null) {
                        if (productEntity.getListDepartmentID().contains(departmentId)) {
                            existDepartment = true;
                            break;
                        }
                    }

                }
                allowedToSave = true;
                if (existDepartment) {
                    for (ProductGroupEntity item : groupEntityList.getProducGroupList()) {
                        ProductEntity productEntity = ListProductManager.getInstance().getProductById(item.getProductDetailID());
                        if (productEntity != null) {
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
                                            view.showError(CoreApplication.getInstance().getString(R.string.text_exceed_the_number_of_requests_in_group));
                                        }
                                    } else {
                                        showError(CoreApplication.getInstance().getString(R.string.text_product_not_in_times));
                                    }
                                }

                            });
                        }


                        if (!allowedToSave) {
                            return;
                        }
                    }

                    saveListWithGroupCode(times, groupEntityList, departmentId);
                } else {
                    showError(CoreApplication.getInstance().getString(R.string.no_product_in_group_to_department));
                }
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
        localRepository.getListLogScanStagesUpdate().subscribe(new Action1<HashMap<List<LogScanStages>, Set<GroupScan>>>() {
            @Override
            public void call(HashMap<List<LogScanStages>, Set<GroupScan>> list) {
                if (list.size() == 0) {
                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_no_data_upload));
                }
                GsonBuilder builder = new GsonBuilder();
                builder.excludeFieldsWithoutExposeAnnotation();
                Gson gson = builder.create();
                for (Map.Entry<List<LogScanStages>, Set<GroupScan>> map : list.entrySet()) {
                    List<GroupScan> scanList = new ArrayList<GroupScan>(map.getValue());
                    checkUpdateForGroupUsecase.executeIO(new CheckUpdateForGroupUsecase.RequestValue(gson.toJson(scanList)),
                            new BaseUseCase.UseCaseCallback<CheckUpdateForGroupUsecase.ResponseValue,
                                    CheckUpdateForGroupUsecase.ErrorValue>() {
                                @Override
                                public void onSuccess(CheckUpdateForGroupUsecase.ResponseValue successResponse) {
                                    scanProductDetailOutUsecase.executeIO(new ScanProductDetailOutUsecase.RequestValue(gson.toJson(map.getKey())),
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

                                @Override
                                public void onError(CheckUpdateForGroupUsecase.ErrorValue errorResponse) {
                                    view.showError(errorResponse.getDescription());
                                    view.hideProgressBar();
                                    if (errorResponse.getEntity() != null) {
                                        ListGroupManager.getInstance().setListGroup(errorResponse.getEntity());
                                        localRepository.addGroupScan(errorResponse.getEntity()).subscribe();
                                    }
                                }
                            });

                }


            }
        });
    }

    @Override
    public void saveBarcodeToDataBase(int times, ProductEntity
            productEntity, int number, int departmentId, GroupEntity groupEntity, boolean typeScan) {
        view.showProgressBar();
        UserEntity user = UserManager.getInstance().getUser();
        String groupCode = null;
        int groupCodeId = -1;
        if (groupEntity != null) {
            groupCode = groupEntity.getGroupCode();
            groupCodeId = groupEntity.getMasterGroupId();
        }
        LogScanStages logScanStages = new LogScanStages(productEntity.getOrderId(), departmentId, user.getRole(), productEntity.getProductDetailID(),
                groupCodeId , groupCode, productEntity.getBarcode(), productEntity.getModule(), number, typeScan, times, ConvertUtils.getDateTimeCurrent(), user.getId(), number);
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
                        ListGroupManager.getInstance().setListGroup(successResponse.getEntity());
                        localRepository.addGroupScan(successResponse.getEntity()).subscribe();
                    }

                    @Override
                    public void onError(GetListProductDetailGroupUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        ListGroupManager.getInstance().setListGroup(new ArrayList<>());
                        //  view.showError(errorResponse.getDescription());
                    }
                });
    }

    @Override
    public void saveListWithGroupCode(int times, GroupEntity groupEntity, int departmentId) {
        for (ProductGroupEntity item : groupEntity.getProducGroupList()) {
            final ProductEntity productEntity = ListProductManager.getInstance().getProductById(item.getProductDetailID());
            if (productEntity != null) {
                saveBarcodeToDataBase(times, productEntity, item.getNumber(), departmentId, groupEntity, false);
            }

        }
    }

    @Override
    public void saveListWithGroupCodeEnough(int times, List<ProductGroupEntity> list, int departmentId) {

    }

    @Override
    public void countListAllData(int orderId) {
        localRepository.countAllDetailWaitingUpload(orderId).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (integer > 0) {
                    view.showDialogUpload();
                }
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
