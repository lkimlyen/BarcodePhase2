package com.demo.barcode.screen.group_code;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.GroupCodeEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.ListGroupCode;
import com.demo.architect.data.model.offline.LogScanStages;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.DeactiveProductDetailGroupUsecase;
import com.demo.architect.domain.GroupProductDetailUsecase;
import com.demo.architect.domain.UpdateProductDetailGroupUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
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
    @Inject
    LocalRepository localRepository;

    @Inject
    GroupCodePresenter(@NonNull GroupCodeContract.View view, GroupProductDetailUsecase groupProductDetailUsecase,
                       UpdateProductDetailGroupUsecase updateProductDetailGroupUsecase,
                       DeactiveProductDetailGroupUsecase deactiveProductDetailGroupUsecase) {
        this.view = view;

        this.groupProductDetailUsecase = groupProductDetailUsecase;
        this.updateProductDetailGroupUsecase = updateProductDetailGroupUsecase;
        this.deactiveProductDetailGroupUsecase = deactiveProductDetailGroupUsecase;
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
    public void getListScanStages(int orderId, int departmentId, int times, String module) {
        localRepository.getListScanStages(orderId, departmentId, times, module).subscribe(new Action1<RealmResults<LogScanStages>>() {
            @Override
            public void call(RealmResults<LogScanStages> logScanStages) {
                view.showListScanStages(logScanStages);
            }
        });
    }

    @Override
    public void getListGroupCode(int orderId, int departmentId, int times, String module) {
        localRepository.getListGroupCode(orderId, departmentId, times, module).subscribe(new Action1<RealmResults<ListGroupCode>>() {
            @Override
            public void call(RealmResults<ListGroupCode> groupCodes) {
                view.showGroupCode(groupCodes);
            }
        });
    }

    @Override
    public void getListOrderDetail(int orderId) {
        SOEntity soEntity = ListSOManager.getInstance().getSOById(orderId);
        view.showSODetail(soEntity);
    }

    @Override
    public void groupCode(int orderId, int departmentId, int times, Collection<LogScanStages> list) {
        view.showProgressBar();
        final LogScanStages[] listSelect = new LogScanStages[list.size()];
        list.toArray(listSelect);
        List<GroupCodeEntity> groupCodeList = new ArrayList<>();
        for (LogScanStages logScanStages : listSelect) {
            GroupCodeEntity groupCodeEntity = new GroupCodeEntity(logScanStages.getOrderId(), logScanStages.getProductDetailId(),
                    logScanStages.getNumberGroup(), logScanStages.getUserId());
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
                        localRepository.addGroupCode(successResponse.getGroupCode(), orderId, departmentId, times, listSelect)
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_group_code_success));
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
    public void updateGroupCode(ListGroupCode groupCode, int orderId, int departmentId, int times, Collection<LogScanStages> list) {
        view.showProgressBar();
        final LogScanStages[] listSelect = new LogScanStages[list.size()];
        list.toArray(listSelect);
        List<GroupCodeEntity> groupCodeList = new ArrayList<>();
        List<GroupCodeEntity> groupCodeUpdateList = new ArrayList<>();
        for (LogScanStages logScanStages : listSelect) {
            int numberInput = 0;
            boolean exist = false;
            for (LogScanStages logCheck : groupCode.getList()) {
                if (logCheck.getBarcode().equals(logScanStages.getBarcode())) {
                    exist = true;
                    numberInput = logCheck.getNumberGroup();
                    break;
                }
            }
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
                        localRepository.addGroupCode(groupCode.getGroupCode(), orderId, departmentId, times, listSelect)
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_group_code_success));
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
    public void updateNumberGroup(int logId, int numberGroup) {
        localRepository.updateNumberGroup(logId, numberGroup).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_update_number_group_success));
            }
        });
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
                                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_detached_code_success));
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
    public void removeItemInGroup(ListGroupCode groupCode, LogScanStages logScanStages,int orderId, int departmentId, int times) {
        view.showProgressBar();
        List<GroupCodeEntity> groupCodeList = new ArrayList<>();
        GroupCodeEntity groupCodeEntity = new GroupCodeEntity(logScanStages.getOrderId(), logScanStages.getProductDetailId(),
                logScanStages.getNumberGroup(), logScanStages.getUserId());
        groupCodeList.add(groupCodeEntity);
        Gson gson = new Gson();
        String json = gson.toJson(groupCodeList);
        UserEntity user = UserManager.getInstance().getUser();
        updateProductDetailGroupUsecase.executeIO(
                new UpdateProductDetailGroupUsecase.RequestValue(groupCode.getGroupCode(), null,
                        null, json, user.getId()),
                new BaseUseCase.UseCaseCallback<UpdateProductDetailGroupUsecase.ResponseValue,
                        UpdateProductDetailGroupUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(UpdateProductDetailGroupUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        localRepository.removeItemInGroup(groupCode, logScanStages,orderId,departmentId,times)
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_remove_item_in_group_code_success));
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


}
