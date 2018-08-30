package com.demo.barcode.screen.create_packaging;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.architect.data.model.offline.ProductPackagingModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetApartmentUsecase;
import com.demo.architect.domain.GetCodePackUsecase;
import com.demo.architect.domain.GetListProductInPackageUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.GetModuleUsecase;
import com.demo.architect.domain.PostCheckBarCodeUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListApartmentManager;
import com.demo.barcode.manager.ListCodePackManager;
import com.demo.barcode.manager.ListModuleManager;
import com.demo.barcode.manager.ListProductPackagingManager;
import com.demo.barcode.manager.ListSOManager;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class CreatePackagingPresenter implements CreatePackagingContract.Presenter {

    private final String TAG = CreatePackagingPresenter.class.getName();
    private final CreatePackagingContract.View view;
    private final GetListSOUsecase getListSOUsecase;
    private final GetApartmentUsecase getApartmentUsecase;
    private final GetModuleUsecase getModuleUsecase;
    private final GetCodePackUsecase getCodePackUsecase;
    private final PostCheckBarCodeUsecase postCheckBarCodeUsecase;
    private final GetListProductInPackageUsecase getListProductInPackageUsecase;

    @Inject
    LocalRepository localRepository;

    @Inject
    CreatePackagingPresenter(@NonNull CreatePackagingContract.View view, GetListSOUsecase getListSOUsecase, GetApartmentUsecase getApartmentUsecase, GetModuleUsecase getModuleUsecase, GetCodePackUsecase getCodePackUsecase, PostCheckBarCodeUsecase postCheckBarCodeUsecase, GetListProductInPackageUsecase getListProductInPackageUsecase) {
        this.view = view;
        this.getListSOUsecase = getListSOUsecase;
        this.getApartmentUsecase = getApartmentUsecase;
        this.getModuleUsecase = getModuleUsecase;
        this.getCodePackUsecase = getCodePackUsecase;
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
    public void getListApartment(int orderId) {
        view.showProgressBar();
        getApartmentUsecase.executeIO(new GetApartmentUsecase.RequestValue(orderId),
                new BaseUseCase.UseCaseCallback<GetApartmentUsecase.ResponseValue,
                        GetApartmentUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetApartmentUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListApartmentManager.getInstance().setListDepartment(successResponse.getEntity());
                        view.showListApartment(successResponse.getEntity());
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
    public void getListModule(int orderId, int orderType, int apartmentId) {
        view.showProgressBar();
        getModuleUsecase.executeIO(new GetModuleUsecase.RequestValue(orderId, orderType, apartmentId),
                new BaseUseCase.UseCaseCallback<GetModuleUsecase.ResponseValue,
                        GetModuleUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetModuleUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        view.showListModule(successResponse.getEntity());
                        ListModuleManager.getInstance().setListModule(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetModuleUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
    }

    @Override
    public void getListScan(int orderId, int productId, int apartmentId, String sttPack) {
        SOEntity soEntity = ListSOManager.getInstance().getSOById(orderId);
        ModuleEntity moduleEntity = ListModuleManager.getInstance().getModuleById(productId);
        ApartmentEntity apartmentEntity = ListApartmentManager.getInstance().getApartmentById(apartmentId);
        CodePackEntity codePackEntity = ListCodePackManager.getInstance().getCodePackById(sttPack);
        localRepository.getListScanPackaging(soEntity, moduleEntity, apartmentEntity, codePackEntity)
                .subscribe(new Action1<LogListSerialPackPagkaging>() {
                    @Override
                    public void call(LogListSerialPackPagkaging log) {
                        view.showListScan(log);
                    }
                });
    }


    @Override
    public void deleteLogScan(int id) {
        localRepository.deleteScanPackaging(id).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_success));
                view.turnOnVibrator();
                view.startMusicSuccess();
            }
        });
    }

    @Override
    public void updateNumberScan(int id, int number) {
        localRepository.updateNumberScanPackaging(id, number).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_update_barcode_success));
                view.turnOnVibrator();
                view.startMusicSuccess();
            }
        });
    }

    @Override
    public void getListCodePack(int orderId, int orderType, int productId) {
        view.showProgressBar();
        getCodePackUsecase.executeIO(new GetCodePackUsecase.RequestValue(orderId, orderType, productId),
                new BaseUseCase.UseCaseCallback<GetCodePackUsecase.ResponseValue,
                        GetCodePackUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetCodePackUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        view.showListCodePack(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetCodePackUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
    }

    @Override
    public void checkBarcode(String barcode, int orderId, int productId, int apartmentId, String packCode, String sttPack) {
        if (barcode.contains(CoreApplication.getInstance().getString(R.string.text_minus))) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_type));
            return;
        }
        if (barcode.length() < 10 || barcode.length() > 13) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_lenght));
            return;
        }

        postCheckBarCodeUsecase.executeIO(new PostCheckBarCodeUsecase.RequestValue(orderId, productId, apartmentId, packCode, sttPack, barcode),
                new BaseUseCase.UseCaseCallback<PostCheckBarCodeUsecase.ResponseValue,
                        PostCheckBarCodeUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(PostCheckBarCodeUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ProductPackagingEntity product = successResponse.getEntity().get(0);
                        localRepository.findProductPackaging(product.getId()).subscribe(new Action1<ProductPackagingModel>() {
                            @Override
                            public void call(ProductPackagingModel productPackagingModel) {
                                if (productPackagingModel == null || productPackagingModel.getNumberRest() > 0) {
                                    saveBarcode(product, barcode, orderId, productId, apartmentId, packCode, sttPack);
                                } else {
                                    showError(CoreApplication.getInstance().getString(R.string.text_number_input_had_enough));
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(PostCheckBarCodeUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
    }

    @Override
    public void getListProduct(int orderId, int productId, int apartmentId, String packCode, String sttPack) {
        view.showProgressBar();
        getListProductInPackageUsecase.executeIO(new GetListProductInPackageUsecase.RequestValue(orderId, productId, apartmentId, packCode, sttPack),
                new BaseUseCase.UseCaseCallback<GetListProductInPackageUsecase.ResponseValue,
                        GetListProductInPackageUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListProductInPackageUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListProductPackagingManager.getInstance().setListProduct(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetListProductInPackageUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListProductPackagingManager.getInstance().setListProduct(new ArrayList<>());
                    }
                });
    }

    @Override
    public boolean countListScanInPack(int sizeList) {
        return ListProductPackagingManager.getInstance().getListProduct().size() == sizeList;
    }

    int numberProduct = 0;

    @Override
    public boolean checkNumberProduct(int orderId, int productId, int apartmentId, String sttPack) {
        numberProduct = 0;
        localRepository.getTotalScanBySerialPack(orderId, apartmentId, productId, sttPack).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                numberProduct = integer;
            }
        });
        return ListProductPackagingManager.getInstance().sumNumber() == numberProduct;
    }

    void saveBarcode(ProductPackagingEntity product, String barcode, int orderId, int apartmentId, int moduleId, String packCode, String serialPack) {
        localRepository.saveBarcodeScanPackaging(product, barcode, orderId, apartmentId, moduleId, packCode, serialPack)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                        view.startMusicSuccess();
                        view.turnOnVibrator();
                    }
                });
    }

    public void showError(String error) {
        view.showError(error);
        view.startMusicError();
        view.turnOnVibrator();
    }

}
