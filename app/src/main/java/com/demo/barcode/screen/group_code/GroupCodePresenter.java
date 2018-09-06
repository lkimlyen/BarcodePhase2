package com.demo.barcode.screen.group_code;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.GroupCodeEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.GroupCode;
import com.demo.architect.data.model.offline.ListGroupCode;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.model.offline.NumberInputModel;
import com.demo.architect.data.model.offline.ProductDetail;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.DeactiveProductDetailGroupUsecase;
import com.demo.architect.domain.GetInputForProductDetailUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.GroupProductDetailUsecase;
import com.demo.architect.domain.UpdateProductDetailGroupUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListDepartmentManager;
import com.demo.barcode.manager.ListProductGroupManager;
import com.demo.barcode.manager.ListProductManager;
import com.demo.barcode.manager.ListSOManager;
import com.demo.barcode.manager.UserManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class GroupCodePresenter implements GroupCodeContract.Presenter {

    private final String TAG = GroupCodePresenter.class.getName();
    private final GroupCodeContract.View view;
    private final GroupProductDetailUsecase groupProductDetailUsecase;
    private final UpdateProductDetailGroupUsecase updateProductDetailGroupUsecase;
    private final DeactiveProductDetailGroupUsecase deactiveProductDetailGroupUsecase;
    private final GetInputForProductDetailUsecase getInputForProductDetail;
    private final GetListSOUsecase getListSOUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    GroupCodePresenter(@NonNull GroupCodeContract.View view, GroupProductDetailUsecase groupProductDetailUsecase,
                       UpdateProductDetailGroupUsecase updateProductDetailGroupUsecase,
                       DeactiveProductDetailGroupUsecase deactiveProductDetailGroupUsecase, GetInputForProductDetailUsecase getInputForProductDetail, GetListSOUsecase getListSOUsecase) {
        this.view = view;

        this.groupProductDetailUsecase = groupProductDetailUsecase;
        this.updateProductDetailGroupUsecase = updateProductDetailGroupUsecase;
        this.deactiveProductDetailGroupUsecase = deactiveProductDetailGroupUsecase;
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

    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    @Override
    public void getListModule(int orderId) {
        List<ProductEntity> list = ListProductManager.getInstance().getListProduct();
        List<String> result = new ArrayList<>();
        for (ProductEntity product : list) {
            if (!result.contains(product.getModule())) {
                result.add(product.getModule());
            }
        }
        view.showListModule(result);
    }


    @Override
    public void getListGroupCode(int orderId, String module) {

    }

    @Override
    public void getGroupCodeScanList(int orderId, String module) {
        localRepository.getListGroupCode(orderId, module).subscribe(new Action1<RealmResults<GroupCode>>() {
            @Override
            public void call(RealmResults<GroupCode> groupCodes) {
                view.showGroupCodeScanList(groupCodes);
            }
        });
    }



//    @Override
//    public void groupCode(int orderId, int departmentId, int times, String module, Collection<LogScanStages> list) {
//        view.showProgressBar();
//        final LogScanStages[] listSelect = new LogScanStages[list.size()];
//        list.toArray(listSelect);
//        List<GroupCodeEntity> groupCodeList = new ArrayList<>();
//        for (LogScanStages logScanStages : listSelect) {
//            GroupCodeEntity groupCodeEntity = new GroupCodeEntity(logScanStages.getOrderId(), logScanStages.getProductDetailId(),
//                    logScanStages.getNumberGroup(), logScanStages.getUserId());
//            groupCodeList.add(groupCodeEntity);
//        }
//        Gson gson = new Gson();
//        String json = gson.toJson(groupCodeList);
//        groupProductDetailUsecase.executeIO(new GroupProductDetailUsecase.RequestValue(json),
//                new BaseUseCase.UseCaseCallback<GroupProductDetailUsecase.ResponseValue,
//                        GroupProductDetailUsecase.ErrorValue>() {
//                    @Override
//                    public void onSuccess(GroupProductDetailUsecase.ResponseValue successResponse) {
//                        view.hideProgressBar();
//                        localRepository.addGroupCode(successResponse.getGroupCode(), orderId, departmentId, times, listSelect)
//                                .subscribe(new Action1<String>() {
//                                    @Override
//                                    public void call(String s) {
//                                        view.backScanStages(CoreApplication.getInstance().getString(R.string.text_group_code_success));
//
//                                    }
//                                });
//                    }
//
//                    @Override
//                    public void onError(GroupProductDetailUsecase.ErrorValue errorResponse) {
//                        view.hideProgressBar();
//                        view.showError(errorResponse.getDescription());
//                    }
//                }
//        );
//    }

    @Override
    public void updateGroupCode(ListGroupCode groupCode, int orderId, int departmentId, int times, Collection<LogScanStages> list) {
        view.showProgressBar();
        final LogScanStages[] listSelect = new LogScanStages[list.size()];
        list.toArray(listSelect);
        List<GroupCodeEntity> groupCodeList = new ArrayList<>();
        List<GroupCodeEntity> groupCodeUpdateList = new ArrayList<>();
        for (LogScanStages logScanStages : listSelect) {
            int numberInput = 0;
            boolean exist = false;
//            for (LogScanStages logCheck : groupCode.getList()) {
//                if (logCheck.getBarcode().equals(logScanStages.getBarcode())) {
//                    exist = true;
//                    numberInput = logCheck.getNumberGroup();
//                    break;
//                }
//            }
            if (exist) {
                GroupCodeEntity groupCodeEntity = new GroupCodeEntity(logScanStages.getOrderId(), logScanStages.getProductDetailId(),
                        logScanStages.getNumberGroup() + numberInput, logScanStages.getUserId());
                groupCodeUpdateList.add(groupCodeEntity);
            } else {
                GroupCodeEntity groupCodeEntity = new GroupCodeEntity(logScanStages.getOrderId(), logScanStages.getProductDetailId(),
                        logScanStages.getNumberGroup(), logScanStages.getUserId());
                groupCodeList.add(groupCodeEntity);
            }

        }
        Gson gson = new Gson();
        String jsonNew = gson.toJson(groupCodeList);
        String jsonUpdate = gson.toJson(groupCodeUpdateList);
        UserEntity user = UserManager.getInstance().getUser();
        updateProductDetailGroupUsecase.executeIO(
                new UpdateProductDetailGroupUsecase.RequestValue(groupCode.getGroupCode(), jsonNew,
                        jsonUpdate, null, user.getId()),
                new BaseUseCase.UseCaseCallback<UpdateProductDetailGroupUsecase.ResponseValue,
                        UpdateProductDetailGroupUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(UpdateProductDetailGroupUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
//                        localRepository.addGroupCode(groupCode.getGroupCode(), orderId, departmentId, times, listSelect)
//                                .subscribe(new Action1<String>() {
//                                    @Override
//                                    public void call(String s) {
//                                        view.backScanStages(CoreApplication.getInstance().getString(R.string.text_group_code_success));
//
//                                    }
//                                });
                    }

                    @Override
                    public void onError(UpdateProductDetailGroupUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                }
        );
    }

    @Override
    public void updateNumberGroup(int productId,int groupId, int numberGroup) {
        ProductEntity productEntity = ListProductManager.getInstance().getProductById(productId);
        localRepository.updateNumberGroup(productEntity,groupId, numberGroup).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean){
                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_update_number_group_success));
                }else {
                    showError(CoreApplication.getInstance().getString(R.string.text_number_bigger_number_request));
                }

            }
        });
    }

    @Override
    public void groupCode(int orderId,  String module, Collection<GroupCode> list) {
        view.showProgressBar();
        final GroupCode[] listSelect = new GroupCode[list.size()];
        list.toArray(listSelect);
        List<GroupCodeEntity> groupCodeList = new ArrayList<>();
        for (GroupCode logScanStages : listSelect) {
            GroupCodeEntity groupCodeEntity = new GroupCodeEntity(logScanStages.getOrderId(), logScanStages.getProductDetailId(),
                    logScanStages.getNumber(), logScanStages.getUserId());
            groupCodeList.add(groupCodeEntity);
        }
        Gson gson = new Gson();
        String json = gson.toJson(groupCodeList);
        groupProductDetailUsecase.executeIO(new GroupProductDetailUsecase.RequestValue(json),
                new BaseUseCase.UseCaseCallback<GroupProductDetailUsecase.ResponseValue,
                        GroupProductDetailUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GroupProductDetailUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        localRepository.addGroupCode(successResponse.getGroupCode(), orderId, module, listSelect)
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.backScanStages(CoreApplication.getInstance().getString(R.string.text_group_code_success));

                                    }
                                });
                    }

                    @Override
                    public void onError(GroupProductDetailUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                }
        );
    }

    @Override
    public void detachedCode(int orderId, int departmentId, int times, ListGroupCode list) {
        view.showProgressBar();
        UserEntity user = UserManager.getInstance().getUser();
        deactiveProductDetailGroupUsecase.executeIO(
                new DeactiveProductDetailGroupUsecase.RequestValue(list.getGroupCode(), user.getId()),
                new BaseUseCase.UseCaseCallback<DeactiveProductDetailGroupUsecase.ResponseValue,
                        DeactiveProductDetailGroupUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(DeactiveProductDetailGroupUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        localRepository.detachedCodeStages(orderId, departmentId, times, list)
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.backScanStages(CoreApplication.getInstance().getString(R.string.text_detached_code_success));
                                    }
                                });
                    }

                    @Override
                    public void onError(DeactiveProductDetailGroupUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                }
        );
    }

    @Override
    public void removeItemInGroup(String groupCode, LogScanStages logScanStages, int orderId, int departmentId, int times) {

    }

//    @Override
//    public void removeItemInGroup(ListGroupCode groupCode, LogScanStages logScanStages, int orderId, int departmentId, int times) {
//        view.showProgressBar();
//        List<GroupCodeEntity> groupCodeList = new ArrayList<>();
//        GroupCodeEntity groupCodeEntity = new GroupCodeEntity(logScanStages.getOrderId(), logScanStages.getProductDetailId(),
//                logScanStages.getNumberGroup(), logScanStages.getUserId());
//        groupCodeList.add(groupCodeEntity);
//        Gson gson = new Gson();
//        String json = gson.toJson(groupCodeList);
//        UserEntity user = UserManager.getInstance().getUser();
//        updateProductDetailGroupUsecase.executeIO(
//                new UpdateProductDetailGroupUsecase.RequestValue(groupCode.getGroupCode(), null,
//                        null, json, user.getId()),
//                new BaseUseCase.UseCaseCallback<UpdateProductDetailGroupUsecase.ResponseValue,
//                        UpdateProductDetailGroupUsecase.ErrorValue>() {
//                    @Override
//                    public void onSuccess(UpdateProductDetailGroupUsecase.ResponseValue successResponse) {
//                        view.hideProgressBar();
//                        localRepository.removeItemInGroup(groupCode, logScanStages, orderId, departmentId, times)
//                                .subscribe(new Action1<String>() {
//                                    @Override
//                                    public void call(String s) {
//                                        view.backScanStages(CoreApplication.getInstance().getString(R.string.text_remove_item_in_group_code_success));
//                                    }
//                                });
//                    }
//
//                    @Override
//                    public void onError(UpdateProductDetailGroupUsecase.ErrorValue errorResponse) {
//                        view.hideProgressBar();
//                        view.showError(errorResponse.getDescription());
//                    }
//                }
//        );
//    }

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
                        //view.clearDataNoProduct(true);
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
                        getListModule(orderId);

                    }

                    @Override
                    public void onError(GetInputForProductDetailUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListProductManager.getInstance().setListProduct(new ArrayList<>());
                        //view.clearDataNoProduct(false);
                    }
                });

    }

    @Override
    public void checkBarcode(String barcode, String module) {
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

                if (!model.getModule().equals(module)){
                    showError(CoreApplication.getInstance().getString(R.string.text_product_not_in_module));
                    return;
                }
                checkBarcode++;
                localRepository.checkProductExistInGroupCode(model).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            showError(CoreApplication.getInstance().getString(R.string.text_product_scanned));
                        } else {
                            localRepository.checkNumberProductInGroupCode(model).subscribe(new Action1<Boolean>() {
                                @Override
                                public void call(Boolean aBoolean) {
                                    if (aBoolean){
                                        showError(CoreApplication.getInstance().getString(R.string.text_number_scan_enough));
                                    }else {
                                        saveProductInGroupCode(model);
                                    }
                                }
                            });

                        }
                    }
                });

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

    public void saveProductInGroupCode(ProductEntity productEntity) {
        localRepository.addGroupCode(productEntity).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                view.startMusicSuccess();
                view.turnOnVibrator();
                view.hideProgressBar();
            }
        });
    }


}
