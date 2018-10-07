package com.demo.barcode.screen.group_code;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.GroupCodeEntity;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.GroupCode;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.DeactiveProductDetailGroupUsecase;
import com.demo.architect.domain.GetInputForProductDetailUsecase;
import com.demo.architect.domain.GetListModuleByOrderUsecase;
import com.demo.architect.domain.GetListProductDetailGroupUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.GroupProductDetailUsecase;
import com.demo.architect.domain.UpdateProductDetailGroupUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListGroupManager;
import com.demo.barcode.manager.ListProductManager;
import com.demo.barcode.manager.ListSOManager;
import com.demo.barcode.manager.UserManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
    private final GetListProductDetailGroupUsecase getListProductDetailGroupUsecase;
    private final GetListModuleByOrderUsecase getListModuleByOrderUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    GroupCodePresenter(@NonNull GroupCodeContract.View view, GroupProductDetailUsecase groupProductDetailUsecase,
                       UpdateProductDetailGroupUsecase updateProductDetailGroupUsecase,
                       DeactiveProductDetailGroupUsecase deactiveProductDetailGroupUsecase, GetInputForProductDetailUsecase getInputForProductDetail, GetListSOUsecase getListSOUsecase, GetListProductDetailGroupUsecase getListProductDetailGroupUsecase, GetListModuleByOrderUsecase getListModuleByOrderUsecase) {
        this.view = view;

        this.groupProductDetailUsecase = groupProductDetailUsecase;
        this.updateProductDetailGroupUsecase = updateProductDetailGroupUsecase;
        this.deactiveProductDetailGroupUsecase = deactiveProductDetailGroupUsecase;
        this.getInputForProductDetail = getInputForProductDetail;
        this.getListSOUsecase = getListSOUsecase;
        this.getListProductDetailGroupUsecase = getListProductDetailGroupUsecase;
        this.getListModuleByOrderUsecase = getListModuleByOrderUsecase;
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
    public void getListModule(long orderId) {
        view.showProgressBar();
        getListModuleByOrderUsecase.executeIO(new GetListModuleByOrderUsecase.RequestValue(orderId),
                new BaseUseCase.UseCaseCallback<GetListModuleByOrderUsecase.ResponseValue, GetListModuleByOrderUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListModuleByOrderUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        view.showListModule(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetListModuleByOrderUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
//
    }


    @Override
    public void getListGroupCode(long orderId) {
        HashMap<String, List<ProductGroupEntity>> result = ListGroupManager.getInstance().getListGroupCode();
        view.showGroupCode(result);
    }

    @Override
    public void getGroupCodeScanList(long orderId) {
        localRepository.getListGroupCodeScan(orderId).subscribe(new Action1<RealmResults<GroupCode>>() {
            @Override
            public void call(RealmResults<GroupCode> groupCodes) {
                view.showGroupCodeScanList(groupCodes);
            }
        });
    }


    @Override
    public void updateGroupCode(String groupCode, long orderId, Collection<GroupCode> list) {
        view.showProgressBar();
        final GroupCode[] listSelect = new GroupCode[list.size()];
        list.toArray(listSelect);
        List<GroupCodeEntity> groupCodeList = new ArrayList<>();

        GroupEntity groupEntity = ListGroupManager.getInstance().getGroupEntityByGroupCode(groupCode);
        for (GroupCode logScanStages : listSelect) {
            for (ProductGroupEntity productGroupEntity : groupEntity.getProducGroupList()) {
               if (productGroupEntity.getProductDetailID() == logScanStages.getProductDetailId()){
                   showError(CoreApplication.getInstance().getString(R.string.text_has_product_exist_in_group));
                   return;
               }
            }
            GroupCodeEntity groupCodeEntity = new GroupCodeEntity(logScanStages.getOrderId(), logScanStages.getProductDetailId(),
                    logScanStages.getNumber(), logScanStages.getUserId());
            groupCodeList.add(groupCodeEntity);
        }
        Gson gson = new Gson();
        String jsonNew = gson.toJson(groupCodeList);
        UserEntity user = UserManager.getInstance().getUser();
        updateProductDetailGroupUsecase.executeIO(
                new UpdateProductDetailGroupUsecase.RequestValue(groupEntity.getMasterGroupId(), jsonNew,
                        null, null, user.getId()),
                new BaseUseCase.UseCaseCallback<UpdateProductDetailGroupUsecase.ResponseValue,
                        UpdateProductDetailGroupUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(UpdateProductDetailGroupUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        localRepository.updateGroupCode(groupCode, orderId, listSelect)
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_group_code_success));
                                        getListGroupCode(orderId);
                                        getListProductDetailInGroupCode(orderId);
                                    }
                                });
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
    public void updateNumberInGroup(String groupCode, long orderId, Collection<ProductGroupEntity> listUpdate) {
        view.showProgressBar();
        long userId = UserManager.getInstance().getUser().getId();
        final ProductGroupEntity[] listSelect = new ProductGroupEntity[listUpdate.size()];
        listUpdate.toArray(listSelect);
        List<GroupCodeEntity> groupCodeList = new ArrayList<>();
        for (ProductGroupEntity groupEntity : listSelect) {
            GroupCodeEntity groupCodeEntity = new GroupCodeEntity(groupEntity.getOrderId(), groupEntity.getProductDetailID(),
                    groupEntity.getNumber(), userId);
            groupCodeList.add(groupCodeEntity);

        }
        GroupEntity groupEntity = ListGroupManager.getInstance().getGroupEntityByGroupCode(groupCode);
        Gson gson = new Gson();
        String jsonUpdate = gson.toJson(groupCodeList);
        UserEntity user = UserManager.getInstance().getUser();
        updateProductDetailGroupUsecase.executeIO(
                new UpdateProductDetailGroupUsecase.RequestValue(groupEntity.getMasterGroupId(), null,
                        jsonUpdate, null, user.getId()),
                new BaseUseCase.UseCaseCallback<UpdateProductDetailGroupUsecase.ResponseValue,
                        UpdateProductDetailGroupUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(UpdateProductDetailGroupUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        getListProductDetailInGroupCode(orderId);
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_update_number_group_success));
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
    public void updateNumberGroup(long productId, long groupId, double numberGroup) {
        localRepository.updateNumberGroup(groupId, numberGroup).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_update_number_group_success));
                }

            }
        });
    }

    @Override
    public void groupCode(long orderId,Collection<GroupCode> list) {
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
                        localRepository.addGroupCode(successResponse.getGroupCode(), orderId, listSelect)
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_group_code_success));
                                        getListProductDetailInGroupCode(orderId);
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
    public void detachedCode(long orderId, String groupCode) {
        view.showProgressBar();
        GroupEntity groupEntity = ListGroupManager.getInstance().getGroupEntityByGroupCode(groupCode);
        UserEntity user = UserManager.getInstance().getUser();
        List<ProductGroupEntity> list = ListGroupManager.getInstance().getListProductByGroupCode(groupCode);
        deactiveProductDetailGroupUsecase.executeIO(
                new DeactiveProductDetailGroupUsecase.RequestValue(groupEntity.getMasterGroupId(), user.getId()),
                new BaseUseCase.UseCaseCallback<DeactiveProductDetailGroupUsecase.ResponseValue,
                        DeactiveProductDetailGroupUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(DeactiveProductDetailGroupUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        localRepository.detachedCodeStages(list, orderId, groupCode)
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_detached_code_success));
                                        getListProductDetailInGroupCode(orderId);
                                        view.setHeightListView();
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
    public void removeItemInGroup(ProductGroupEntity groupCode, long orderId) {
        view.showProgressBar();
        GroupEntity groupEntity = ListGroupManager.getInstance().getGroupEntityByGroupCode(groupCode.getGroupCode());
        List<GroupCodeEntity> groupCodeList = new ArrayList<>();
        long userId = UserManager.getInstance().getUser().getId();
        GroupCodeEntity groupCodeEntity = new GroupCodeEntity(groupCode.getOrderId(), groupCode.getProductDetailID(),
                groupCode.getNumber(), userId);
        groupCodeList.add(groupCodeEntity);
        Gson gson = new Gson();
        String json = gson.toJson(groupCodeList);
        UserEntity user = UserManager.getInstance().getUser();
        updateProductDetailGroupUsecase.executeIO(
                new UpdateProductDetailGroupUsecase.RequestValue(groupEntity.getMasterGroupId(), null,
                        null, json, user.getId()),
                new BaseUseCase.UseCaseCallback<UpdateProductDetailGroupUsecase.ResponseValue,
                        UpdateProductDetailGroupUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(UpdateProductDetailGroupUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        localRepository.removeItemInGroup(groupCode, orderId)
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_remove_item_in_group_code_success));
                                        getListProductDetailInGroupCode(orderId);
                                    }
                                });
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
    public void getListProduct(long orderId) {
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
    public void checkBarcode(String barcode) {
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
            // ProductGroupEntity productGroupEntity = ListGroupManager.getInstance().getProductById(model.getProductDetailID());
            localRepository.checkNumberProductInGroupCode(model).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean aBoolean) {
                    if (aBoolean && ListGroupManager.getInstance().totalNumberProductGroup(model.getProductDetailID()) < model.getNumberTotalOrder()) {
                        saveProductInGroupCode(model);

                    } else {
                        showError(CoreApplication.getInstance().getString(R.string.text_number_scan_enough));
                    }
                }
            });

        } else {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_no_exist));
        }

    }

    @Override
    public void getListProductDetailInGroupCode(long orderId) {
        view.showProgressBar();
        getListProductDetailGroupUsecase.executeIO(new GetListProductDetailGroupUsecase.RequestValue(orderId),
                new BaseUseCase.UseCaseCallback<GetListProductDetailGroupUsecase.ResponseValue, GetListProductDetailGroupUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListProductDetailGroupUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListGroupManager.getInstance().setListGroup(successResponse.getEntity());
                        getListGroupCode(orderId);
                    }

                    @Override
                    public void onError(GetListProductDetailGroupUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        ListGroupManager.getInstance().setListGroup(new ArrayList<>());
                        view.showGroupCode(new HashMap<>());
                    }
                });
    }

    @Override
    public void deleteScanGroupCode(long id) {
        localRepository.deleteScanGroupCode(id).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_success));
            }
        });
    }
    private  double totalNumberScanGroup;

    @Override
    public double totalNumberScanGroup(long productDetailId) {
        localRepository.totalNumberScanGroup(productDetailId).subscribe(new Action1<Double>() {
            @Override
            public void call(Double aDouble) {
                totalNumberScanGroup = aDouble;
            }
        });
        return totalNumberScanGroup;
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
                view.setHeightListView();
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                view.startMusicSuccess();
                view.turnOnVibrator();
                view.hideProgressBar();
            }
        });
    }


}
