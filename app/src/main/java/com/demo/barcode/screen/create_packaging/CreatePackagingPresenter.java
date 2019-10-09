package com.demo.barcode.screen.create_packaging;

import androidx.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.ListModuleEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.PositionScan;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.Result;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.architect.data.model.offline.ProductPackagingModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetApartmentUsecase;
import com.demo.architect.domain.GetListProductInPackageUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListApartmentManager;
import com.demo.barcode.manager.ListModulePackagingManager;
import com.demo.barcode.manager.PositionScanManager;
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
    private final GetListProductInPackageUsecase getListProductInPackageUsecase;
    private PositionScan positionScan;
    @Inject
    LocalRepository localRepository;

    @Inject
    CreatePackagingPresenter(@NonNull CreatePackagingContract.View view,
                             GetListSOUsecase getListSOUsecase, GetApartmentUsecase getApartmentUsecase,
                             GetListProductInPackageUsecase getListProductInPackageUsecase) {
        this.view = view;
        this.getListSOUsecase = getListSOUsecase;
        this.getApartmentUsecase = getApartmentUsecase;
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
    public void getListScan() {
        localRepository.deleteAllItemLogScanPackaging().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                localRepository.getListScanPackaging()
                        .subscribe(new Action1<RealmResults<LogListSerialPackPagkaging>>() {
                            @Override
                            public void call(RealmResults<LogListSerialPackPagkaging> logListModulePagkagings) {
                                view.showListScan(logListModulePagkagings);
                            }
                        });
            }
        });

    }


    @Override
    public void deleteLogScan(long productId, long logId, String sttPack, String codePack) {
        String module = ListModulePackagingManager.getInstance().getModuleByModule(productId).getModule();
        if (positionScan != null) {

            if (!positionScan.getModule().equals(module) || !positionScan.getSerialPack().equals(sttPack)) {
                showError(CoreApplication.getInstance().getString(R.string.text_incomplete_pack));
                return;
            }
        }
        localRepository.deleteScanPackaging(productId, sttPack, codePack, logId).subscribe(new Action1<String>() {
            @Override
            public void call(String total) {
                localRepository.getTotalScanBySerialPack(productId, sttPack).subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        if (integer == 0) {
                            if (positionScan != null) {
                                positionScan = null;
                            }
                        } else {
                            if (positionScan == null) {
                                positionScan = new PositionScan(module, sttPack, codePack);
                            }
                        }

                        PositionScanManager.getInstance().setPositionScan(positionScan);
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_success));
                        view.turnOnVibrator();
                        view.startMusicSuccess();
                    }
                });
            }
        });
    }

    @Override
    public void updateNumberScan(long productId, long logId, int number, String sttPack, String codePack, int numberTotal) {
        String module = ListModulePackagingManager.getInstance().getModuleByModule(productId).getModule();
        localRepository.updateNumberScanPackaging(logId, number).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

                localRepository.getTotalScanBySerialPack(productId,
                        sttPack).subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        if (numberTotal == integer) {
                            positionScan = null;
                        } else {
                            positionScan = new PositionScan(module,
                                    sttPack, codePack
                            );
                            // PositionScanManager.getInstance().addPositionScan(logScanPackaging.getOrderId(), logScanPackaging.getProductSetId(), positionScan);
                        }
                        PositionScanManager.getInstance().setPositionScan(positionScan);
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_update_barcode_success));
                        view.turnOnVibrator();
                        view.startMusicSuccess();
                    }
                });
            }
        });
    }

    private Result resultFind;

    @Override
    public void checkBarcode(String barcode, long orderId, long apartmentId) {
        barcode = barcode.toUpperCase();
        if (barcode.contains(CoreApplication.getInstance().getString(R.string.text_minus))) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_type));
            return;
        }
        List<Result> resultList = new ArrayList<>();
        if (positionScan != null) {
            resultList = ListModulePackagingManager.getInstance().getListProductByBarcodeAndModuleAndSttPack(barcode, positionScan.getModule(), positionScan.getSerialPack());
        } else {
            resultList = ListModulePackagingManager.getInstance().getListProductByBarcode(barcode);
        }

        if (resultList.size() == 0) {
            resultList = ListModulePackagingManager.getInstance().getListProductByBarcode(barcode);
            if (resultList.size() == 0) {
                showError(CoreApplication.getInstance().getString(R.string.text_barcode_no_exist));
            } else {
                showError(CoreApplication.getInstance().getString(R.string.text_incomplete_pack));
            }
            return;
        }
        resultFind = null;
        if (resultList.size() == 1) {
            resultFind = resultList.get(0);
            checkBarcode(orderId, apartmentId);
        } else {
            localRepository.findProductPackagingByList(resultList).subscribe(new Action1<Result>() {
                @Override
                public void call(Result result) {
                    resultFind = result;
                    if (resultFind == null) {
                        showError(CoreApplication.getInstance().getString(R.string.text_pack_scan_enough));
                    } else {
                        checkBarcode(orderId, apartmentId);
                    }
                }
            });


        }
        Log.d("bambi", "3");
    }

    private void checkBarcode(long orderId, long apartmentId) {
        PackageEntity packageEntity = resultFind.getPackageEntity();
        ListModuleEntity listModuleEntity = resultFind.getListModuleEntity();
        ProductPackagingEntity productPackagingEntity = resultFind.getProductPackagingEntity();
        if (packageEntity.getNumberScan() == listModuleEntity.getNumberRequired()) {
            showError(CoreApplication.getInstance().getString(R.string.text_pack_scan_enough));
            //  return;
        } else {
            saveBarcode(listModuleEntity, packageEntity, productPackagingEntity, orderId, apartmentId);
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
        localRepository.findProductPackaging(listModuleEntity.getProductId(), productPackagingEntity.getId(), packageEntity.getSerialPack()).subscribe(new Action1<ProductPackagingModel>() {
            @Override
            public void call(ProductPackagingModel productPackagingModel) {
                if (productPackagingModel == null || productPackagingModel.getNumberRest() > 0) {
                    localRepository.saveBarcodeScanPackaging(listModuleEntity, packageEntity, productPackagingEntity, orderId, apartmentId)
                            .subscribe(new Action1<Integer>() {
                                @Override
                                public void call(Integer integer) {

                                    if (packageEntity.getTotal() == integer) {
                                        positionScan = null;
                                    } else {
                                        positionScan = new PositionScan(listModuleEntity.getModule(), packageEntity.getSerialPack(), packageEntity.getPackCode());
                                    }
                                    PositionScanManager.getInstance().setPositionScan(positionScan);

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
