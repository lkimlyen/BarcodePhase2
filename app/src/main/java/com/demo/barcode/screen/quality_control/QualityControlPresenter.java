package com.demo.barcode.screen.quality_control;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.NumberInput;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.ImageModel;
import com.demo.architect.data.model.offline.QualityControlModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.AddLogQCUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetInputForProductDetailUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.UploadImageUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListDepartmentManager;
import com.demo.barcode.manager.ListProductManager;
import com.demo.barcode.manager.ListSOManager;
import com.demo.barcode.manager.UserManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class QualityControlPresenter implements QualityControlContract.Presenter {

    private final String TAG = QualityControlPresenter.class.getName();
    private final QualityControlContract.View view;
    private final GetListSOUsecase getListSOUsecase;
    private final GetInputForProductDetailUsecase getInputForProductDetail;
    private final UploadImageUsecase uploadImageUsecase;
    private final AddLogQCUsecase addLogQCUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    QualityControlPresenter(@NonNull QualityControlContract.View view, GetListSOUsecase getListSOUsecase, GetInputForProductDetailUsecase getInputForProductDetailUsecase, UploadImageUsecase uploadImageUsecase, AddLogQCUsecase addLogQCUsecase) {
        this.view = view;
        this.getListSOUsecase = getListSOUsecase;
        this.getInputForProductDetail = getInputForProductDetailUsecase;
        this.uploadImageUsecase = uploadImageUsecase;
        this.addLogQCUsecase = addLogQCUsecase;
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
                    }
                });
    }

    @Override
    public void getListProduct(long orderId) {
        view.showProgressBar();
        UserEntity userEntity = UserManager.getInstance().getUser();
        getInputForProductDetail.executeIO(new GetInputForProductDetailUsecase.RequestValue(orderId, userEntity.getRole()),
                new BaseUseCase.UseCaseCallback<GetInputForProductDetailUsecase.ResponseValue,
                        GetInputForProductDetailUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetInputForProductDetailUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListProductManager.getInstance().setListProduct(successResponse.getEntity());
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_list_detail_success));
                    }

                    @Override
                    public void onError(GetInputForProductDetailUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListProductManager.getInstance().setListProduct(new ArrayList<>());
                    }
                });
    }

    @Override
    public void checkBarcode(String barcode, long orderId) {
        UserEntity userEntity = UserManager.getInstance().getUser();
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
                if (model.getListDepartmentID().contains(userEntity.getRole())) {
                    saveBarcode(orderId, userEntity.getRole(), model);
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

    @Override
    public void getListQualityControl(long orderId) {
        UserEntity userEntity = UserManager.getInstance().getUser();
        localRepository.getListQualityControl(orderId, userEntity.getRole()).subscribe(new Action1<RealmResults<QualityControlModel>>() {
            @Override
            public void call(RealmResults<QualityControlModel> qualityControlModels) {
                view.showListQualityControl(qualityControlModels);
            }
        });
    }

    @Override
    public void removeItemQualityControl(long id) {

    }

    private int positionList = 0;
    private int positionImage = 0;

    @Override
    public void uploadData() {
        view.showProgressBar();
        localRepository.getListQualityControlUpload().subscribe(new Action1<List<QualityControlModel>>() {
            @Override
            public void call(List<QualityControlModel> qualityControlModels) {
                if (qualityControlModels.size() == positionList) {
                    GsonBuilder builder = new GsonBuilder();
                    builder.excludeFieldsWithoutExposeAnnotation();
                    Gson gson = builder.create();
                    String json = gson.toJson(qualityControlModels);
                    addLogQCUsecase.executeIO(new AddLogQCUsecase.RequestValue(json),
                            new BaseUseCase.UseCaseCallback<AddLogQCUsecase.ResponseValue, AddLogQCUsecase.ErrorValue>() {
                                @Override
                                public void onSuccess(AddLogQCUsecase.ResponseValue successResponse) {
                                    view.hideProgressBar();
                                    localRepository.updateStatusQC().subscribe(new Action1<String>() {
                                        @Override
                                        public void call(String s) {
                                            view.showSuccess(CoreApplication.getInstance().getString(R.string.text_upload_success));
                                        }
                                    });
                                }

                                @Override
                                public void onError(AddLogQCUsecase.ErrorValue errorResponse) {
                                    view.hideProgressBar();
                                    view.showError(errorResponse.getDescription());
                                }
                            });
                } else {
                    positionList = 0;
                    positionImage = 0;
                    uploadImage(qualityControlModels);
                }

            }
        });
    }

    public void showError(String error) {
        view.showError(error);
        view.startMusicError();
        view.turnOnVibrator();
    }

    public void saveBarcode(long orderId, int departmentId, ProductEntity productEntity) {
        localRepository.saveBarcodeQC(orderId, departmentId, productEntity).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                view.startMusicSuccess();
                view.turnOnVibrator();
            }
        });
    }

    public void uploadImage(List<QualityControlModel> qualityControlModels) {

        QualityControlModel qualityControlModel = qualityControlModels.get(positionList);
        ImageModel imageModel = qualityControlModel.getImageList().get(positionImage);
        File file = new File(imageModel.getPathFile());
        UserEntity userEntity = UserManager.getInstance().getUser();
        uploadImageUsecase.executeIO(new UploadImageUsecase.RequestValue(file, qualityControlModel.getOrderId(),
                        qualityControlModel.getDepartmentId(), file.getName(), userEntity.getId()),
                new BaseUseCase.UseCaseCallback<UploadImageUsecase.ResponseValue, UploadImageUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(UploadImageUsecase.ResponseValue successResponse) {
                        localRepository.updateImageIdAndStatus(qualityControlModel.getId(), imageModel.getId(), successResponse.getImageId())
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        if (positionImage < qualityControlModel.getImageList().size() - 1) {
                                            positionImage++;
                                            uploadImage(qualityControlModels);
                                        } else {
                                            positionImage = 0;
                                            positionList++;
                                            if (positionList <= qualityControlModels.size() - 1) {
                                                uploadImage(qualityControlModels);
                                            } else {
                                                uploadData();
                                            }
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onError(UploadImageUsecase.ErrorValue errorResponse) {
                        view.showError(errorResponse.getDescription());
                        view.hideProgressBar();
                    }
                });
    }
}
