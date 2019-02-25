package com.demo.barcode.screen.create_packaging;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.ListModuleEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.PositionScan;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.Result;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.architect.data.model.offline.ProductPackagingModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetApartmentUsecase;
import com.demo.architect.domain.GetListProductInPackageUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.PostCheckBarCodeUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListApartmentManager;
import com.demo.barcode.manager.ListModulePackagingManager;
import com.demo.barcode.manager.ListPositionScanManager;
import com.demo.barcode.manager.ListSOManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class CreatePackagingPresenter implements CreatePackagingContract.Presenter {

    private final String TAG = CreatePackagingPresenter.class.getName();
    private final CreatePackagingContract.View view;
    private final GetListSOUsecase getListSOUsecase;
    private final GetApartmentUsecase getApartmentUsecase;
    private final PostCheckBarCodeUsecase postCheckBarCodeUsecase;
    private final GetListProductInPackageUsecase getListProductInPackageUsecase;
    private PositionScan positionScan;
    @Inject
    LocalRepository localRepository;

    @Inject
    CreatePackagingPresenter(@NonNull CreatePackagingContract.View view, GetListSOUsecase getListSOUsecase, GetApartmentUsecase getApartmentUsecase, PostCheckBarCodeUsecase postCheckBarCodeUsecase, GetListProductInPackageUsecase getListProductInPackageUsecase) {
        this.view = view;
        this.getListSOUsecase = getListSOUsecase;
        this.getApartmentUsecase = getApartmentUsecase;
        this.postCheckBarCodeUsecase = postCheckBarCodeUsecase;
        this.getListProductInPackageUsecase = getListProductInPackageUsecase;
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
    public void getListApartment(long orderId) {
        view.showProgressBar();
        getApartmentUsecase.executeIO(new GetApartmentUsecase.RequestValue(orderId),
                new BaseUseCase.UseCaseCallback<GetApartmentUsecase.ResponseValue,
                        GetApartmentUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetApartmentUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListApartmentManager.getInstance().setListDepartment(successResponse.getEntity());
                        view.showListApartment(successResponse.getEntity());
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_data_success));
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
    public void getListScan(long orderId, long apartmentId) {
        positionScan = ListPositionScanManager.getInstance().getPositionScanByOrderId(orderId, apartmentId);
        SOEntity soEntity = ListSOManager.getInstance().getSOById(orderId);
        ApartmentEntity apartmentEntity = ListApartmentManager.getInstance().getApartmentById(apartmentId);
        localRepository.getListScanPackaging(soEntity, apartmentEntity)
                .subscribe(new Action1<RealmResults<LogListSerialPackPagkaging>>() {
                    @Override
                    public void call(RealmResults<LogListSerialPackPagkaging> logListModulePagkagings) {
                        view.showListScan(logListModulePagkagings);
                    }
                });
    }


    @Override
    public void deleteLogScan(LogScanPackaging log) {
        if (positionScan != null) {
            ListModuleEntity listModuleEntity = ListModulePackagingManager.getInstance().getModuleById(log.getModule());
            if (!positionScan.getModule().equals(listModuleEntity.getModule()) || !positionScan.getSerialPack().equals(log.getSttPack())) {
                showError(CoreApplication.getInstance().getString(R.string.text_incomplete_pack));
                return;
            }
        }
        localRepository.deleteScanPackaging(log.getId()).subscribe(new Action1<String>() {
            @Override
            public void call(String total) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_success));
                view.turnOnVibrator();
                view.startMusicSuccess();
                localRepository.getTotalScanBySerialPack(log.getOrderId(), log.getApartmentId(), log.getModule(), log.getSttPack()).subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        if (integer == 0) {
                            if (positionScan != null) {
                                ListPositionScanManager.getInstance().removeList(log.getOrderId(), log.getApartmentId());
                                positionScan = null;
                            }
                        } else {
                            ListModuleEntity listModuleEntity = ListModulePackagingManager.getInstance().getModuleById(log.getModule());
                            if (positionScan == null) {
                                positionScan = new PositionScan(log.getOrderId(), log.getApartmentId(), listModuleEntity.getModule(), log.getSttPack(), log.getCodePack());
                                ListPositionScanManager.getInstance().addPositionScan(log.getOrderId(), log.getApartmentId(), positionScan);
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void updateNumberScan(LogScanPackaging logScanPackaging, int number) {
        String module = ListModulePackagingManager.getInstance().getModuleByModule(logScanPackaging.getModule()).getModule();

        localRepository.updateNumberScanPackaging(logScanPackaging.getId(), number).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

                localRepository.getTotalScanBySerialPack(logScanPackaging.getOrderId(), logScanPackaging.getApartmentId(), logScanPackaging.getModule(),
                        logScanPackaging.getSttPack()).subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        if (logScanPackaging.getProductPackagingModel().getNumberTotal() == integer) {

                            positionScan = null;
                            ListPositionScanManager.getInstance().removeList(logScanPackaging.getOrderId(), logScanPackaging.getApartmentId());
                        } else {
                            positionScan = new PositionScan(logScanPackaging.getOrderId(), logScanPackaging.getApartmentId(), module,
                                    logScanPackaging.getSttPack(), logScanPackaging.getCodePack()
                            );

                            ListPositionScanManager.getInstance().addPositionScan(logScanPackaging.getOrderId(), logScanPackaging.getApartmentId(), positionScan);
                        }

                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_update_barcode_success));
                        view.turnOnVibrator();
                        view.startMusicSuccess();
                    }
                });
            }
        });
    }

    private Result resultFind;

    private int numberLoop = 0;
    PackageEntity packageEntity;
    ListModuleEntity listModuleEntity;
    ProductPackagingEntity productPackagingEntity;
    @Override
    public void checkBarcode(String barcode, long orderId, long apartmentId) {
        numberLoop = 0;
        if (barcode.contains(CoreApplication.getInstance().getString(R.string.text_minus))) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_type));
            return;
        }
        List<Result> resultList = ListModulePackagingManager.getInstance().getListProductByBarcode(barcode);
        if (resultList.size() == 0) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_no_exist));
            return;
        }


        resultFind = null;

        Log.d("bambi", "1");
        if (resultList.size() == 1) {
            resultFind = resultList.get(0);
            checkBarcode(orderId,apartmentId);
        } else {
            Log.d("bambi", "2");
            for (Result result : resultList) {
                numberLoop++;
                localRepository.findProductPackaging(result.getProductPackagingEntity().getId(), result.getPackageEntity().getSerialPack()).subscribe(new Action1<ProductPackagingModel>() {
                    @Override
                    public void call(ProductPackagingModel productPackagingModel) {
                        Log.d("bambi", "loop");
                        if ((productPackagingModel == null || productPackagingModel.getNumberRest() > 0) && result.getPackageEntity().getNumberScan() < result.getListModuleEntity().getNumberRequired()) {
                            if (resultFind == null){
                                resultFind = result;
                            }
                        } else {
                            if (positionScan != null && resultFind == null) {
                                if (!positionScan.getCodePack().equals(result.getPackageEntity().getPackCode())) {
                                    if (!result.getListModuleEntity().getModule().equals(positionScan.getModule()) || !result.getPackageEntity().getSerialPack().equals(positionScan.getSerialPack())) {
                                        resultFind = new Result();
                                        // return;
                                    }
                                }

                            }
                        }

                        if (numberLoop == resultList.size()){
                            if (resultFind == null) {
                                showError(CoreApplication.getInstance().getString(R.string.text_pack_scan_enough));
                                //return;
                            }else {
                                if (resultFind.getListModuleEntity() == null) {
                                    showError(CoreApplication.getInstance().getString(R.string.text_incomplete_pack));
                                    //return;
                                }else {
                                    checkBarcode(orderId,apartmentId);
                                }

                            }
                        }

                    }
                });

            }

        }
        Log.d("bambi", "3");
    }

    private void checkBarcode(long orderId, long apartmentId){
        packageEntity = resultFind.getPackageEntity();
        listModuleEntity = resultFind.getListModuleEntity();
        productPackagingEntity = resultFind.getProductPackagingEntity();
        if (positionScan != null) {
            ListModuleEntity moduleEntity = ListModulePackagingManager.getInstance().getModuleByModule(positionScan.getModule());
            PackageEntity packageEntity1 = ListModulePackagingManager.getInstance().getPackingBySerialPack(positionScan.getModule(), positionScan.getSerialPack());
            localRepository.getTotalScanBySerialPack(orderId, apartmentId, moduleEntity.getProductId(), positionScan.getSerialPack()).subscribe(new Action1<Integer>() {
                @Override
                public void call(Integer integer) {
                    if (packageEntity1.getTotal() == integer) {
                        if (packageEntity.getSerialPack().equals(positionScan.getSerialPack()) && listModuleEntity.getModule().equals(positionScan.getModule())) {
                            showError(CoreApplication.getInstance().getString(R.string.text_number_input_had_enough));
                            // return;
                        } else {
                            if (packageEntity.getNumberScan() == listModuleEntity.getNumberRequired()) {
                                showError(CoreApplication.getInstance().getString(R.string.text_pack_scan_enough));
                                //  return;
                            } else {
                                saveBarcode(listModuleEntity, packageEntity, productPackagingEntity, orderId, apartmentId);
                            }
                        }
                    } else {
                        if (!listModuleEntity.getModule().equals(positionScan.getModule()) || !packageEntity.getSerialPack().equals(positionScan.getSerialPack())) {
                            showError(CoreApplication.getInstance().getString(R.string.text_incomplete_pack));
                            //  return;
                        } else {
                            saveBarcode(listModuleEntity, packageEntity, productPackagingEntity, orderId, apartmentId);
                        }

                    }
                }
            });
        } else {
            if (packageEntity.getNumberScan() == listModuleEntity.getNumberRequired()) {
                showError(CoreApplication.getInstance().getString(R.string.text_pack_scan_enough));
                //  return;
            }else {

                saveBarcode(listModuleEntity, packageEntity, productPackagingEntity, orderId, apartmentId);
            }
        }
    }

    @Override
    public void getListProduct(long orderId, long apartmentId) {
        view.showProgressBar();
        getListProductInPackageUsecase.executeIO(new GetListProductInPackageUsecase.RequestValue(orderId, apartmentId),
                new BaseUseCase.UseCaseCallback<GetListProductInPackageUsecase.ResponseValue,
                        GetListProductInPackageUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListProductInPackageUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListModulePackagingManager.getInstance().setList(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetListProductInPackageUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListModulePackagingManager.getInstance().setList(new ArrayList<>());
                    }
                });
    }

    @Override
    public boolean countListScanInPack(int sizeList) {
        return ListModulePackagingManager.getInstance().getList().size() == sizeList;
    }


    @Override
    public void deleteAllItemLog() {
        localRepository.deleteAllItemLogScanPackaging().subscribe(new Action1<String>() {
            @Override
            public void call(String integer) {

            }
        });
    }

    public void showError(String error) {
        view.showError(error);
        view.startMusicError();
        view.turnOnVibrator();
    }

    public void saveBarcode(ListModuleEntity listModuleEntity, PackageEntity packageEntity, ProductPackagingEntity productPackagingEntity, long orderId, long apartmentId) {
        localRepository.findProductPackaging(productPackagingEntity.getId(), packageEntity.getSerialPack()).subscribe(new Action1<ProductPackagingModel>() {
            @Override
            public void call(ProductPackagingModel productPackagingModel) {
                if (productPackagingModel == null || productPackagingModel.getNumberRest() > 0) {
                    localRepository.saveBarcodeScanPackaging(listModuleEntity, packageEntity, productPackagingEntity, orderId, apartmentId)
                            .subscribe(new Action1<String>() {
                                @Override
                                public void call(String s) {

                                    localRepository.getTotalScanBySerialPack(orderId, apartmentId, listModuleEntity.getProductId(), packageEntity.getSerialPack()).subscribe(new Action1<Integer>() {
                                        @Override
                                        public void call(Integer integer) {
                                            if (packageEntity.getTotal() == integer) {

                                                positionScan = null;
                                                ListPositionScanManager.getInstance().removeList(orderId, apartmentId);
                                            } else {
                                                positionScan = new PositionScan(orderId, apartmentId, listModuleEntity.getModule(), packageEntity.getSerialPack(), packageEntity.getPackCode());

                                                ListPositionScanManager.getInstance().addPositionScan(orderId, apartmentId, positionScan);
                                            }
                                        }
                                    });

                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                                    view.startMusicSuccess();
                                    view.turnOnVibrator();
                                    view.setHeightListView();
                                }
                            });
                } else {
                    showError(CoreApplication.getInstance().getString(R.string.text_number_input_had_enough));
                }
            }
        });

    }

}
