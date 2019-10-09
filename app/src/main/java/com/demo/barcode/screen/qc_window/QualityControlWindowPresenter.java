package com.demo.barcode.screen.qc_window;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.MachineEntity;
import com.demo.architect.data.model.ProductWindowEntity;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.ImageModel;
import com.demo.architect.data.model.offline.QualityControlWindowModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.AddLogQCWindowUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetInputForProductDetailWindowUsecase;
import com.demo.architect.domain.GetListQCUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.UploadImageUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListMachineManager;
import com.demo.barcode.manager.ListProductManager;
import com.demo.barcode.manager.ListProductWindowManager;
import com.demo.barcode.manager.ListSOManager;
import com.demo.barcode.manager.UserManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class QualityControlWindowPresenter implements QualityControlWindowContract.Presenter {

    private final String TAG = QualityControlWindowPresenter.class.getName();
    private final QualityControlWindowContract.View view;
    private final GetListSOUsecase getListSOUsecase;
    private final GetInputForProductDetailWindowUsecase getInputForProductDetail;
    private final UploadImageUsecase uploadImageUsecase;
    private final AddLogQCWindowUsecase addLogQCUsecase;
    private final GetListQCUsecase getListQCUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    QualityControlWindowPresenter(@NonNull QualityControlWindowContract.View view,
                                  GetListSOUsecase getListSOUsecase,
                                  GetInputForProductDetailWindowUsecase getInputForProductDetailUsecase,
                                  UploadImageUsecase uploadImageUsecase,
                                  AddLogQCWindowUsecase addLogQCUsecase, GetListQCUsecase getListQCUsecase) {
        this.view = view;
        this.getListSOUsecase = getListSOUsecase;
        this.getInputForProductDetail = getInputForProductDetailUsecase;
        this.uploadImageUsecase = uploadImageUsecase;
        this.addLogQCUsecase = addLogQCUsecase;
        this.getListQCUsecase = getListQCUsecase;
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
    public void getListSO() {
        view.showProgressBar();
        getListSOUsecase.executeIO(new GetListSOUsecase.RequestValue(UserManager.getInstance().getUser().getOrderType()),
                new BaseUseCase.UseCaseCallback<GetListSOUsecase.ResponseValue,
                        GetListSOUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListSOUsecase.ResponseValue successResponse) {
                        view.showListSO(successResponse.getEntity());
                        ListSOManager.getInstance().setListSO(successResponse.getEntity());
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_so_success));
                        getListQC();
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
    public void getListMachine() {
        List<MachineEntity> list = ListMachineManager.getInstance().getMachineByRole(UserManager.getInstance().getUser().getRole());
        view.showListMachine(list);
    }

    @Override
    public void getListQC() {

        getListQCUsecase.executeIO(new GetListQCUsecase.RequestValue(),
                new BaseUseCase.UseCaseCallback<GetListQCUsecase.ResponseValue,
                        GetListQCUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListQCUsecase.ResponseValue successResponse) {
                        view.showListQC(successResponse.getEntity());
                        view.hideProgressBar();
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_list_qc_success));
                    }

                    @Override
                    public void onError(GetListQCUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
    }

    @Override
    public void getListProduct(long orderId) {
        view.showProgressBar();
        UserEntity userEntity = UserManager.getInstance().getUser();
        getInputForProductDetail.executeIO(new GetInputForProductDetailWindowUsecase.RequestValue(orderId, userEntity.getRole()),
                new BaseUseCase.UseCaseCallback<GetInputForProductDetailWindowUsecase.ResponseValue,
                        GetInputForProductDetailWindowUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetInputForProductDetailWindowUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListProductWindowManager.getInstance().setListProduct(successResponse.getEntity());
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_list_detail_success));
                    }

                    @Override
                    public void onError(GetInputForProductDetailWindowUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListProductManager.getInstance().setListProduct(new ArrayList<>());
                    }
                });
    }

    @Override
    public void checkBarcode(String barcode, long orderId, int machineId, String violator, int qcId) {
        barcode = barcode.toUpperCase();


        List<ProductWindowEntity> list = ListProductWindowManager.getInstance().getListProduct();

        if (list.size() == 0) {
            showError(CoreApplication.getInstance().getString(R.string.text_product_empty));
            return;
        }

        ProductWindowEntity productWindowEntity = ListProductWindowManager.getInstance().getProductByBarcode(barcode);

        if (productWindowEntity != null) {

            if (productWindowEntity.getNumberWaitting() > 0) {
                localRepository.checkBarcodeExistInQCWindow(barcode).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean exist) {
                        if (!exist) {
                            saveBarcode(machineId, violator, qcId, productWindowEntity);
                        } else {
                            showError(CoreApplication.getInstance().getString(R.string.text_product_in_qc));
                        }
                    }
                });
            } else {
                showError(CoreApplication.getInstance().getString(R.string.text_product_delivery));
            }

        } else {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_no_exist));
        }


    }

    @Override
    public void getListQualityControl() {
        localRepository.deleteAlLQC(UserManager.getInstance().getUser().getOrderType()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                localRepository.getListQualityControlWindow().subscribe(new Action1<RealmResults<QualityControlWindowModel>>() {
                    @Override
                    public void call(RealmResults<QualityControlWindowModel> qualityControlModels) {
                        view.showListQualityControl(qualityControlModels);
                    }
                });
            }
        });

    }

    @Override
    public void removeItemQualityControl(long id) {
        localRepository.deleteQC(id, UserManager.getInstance().getUser().getOrderType()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_success));view.refreshLayout();
            }
        });
    }

    private int positionList = 0;
    private int positionImage = 0;

    @Override
    public void uploadData(int machineId, String violator, int qcId, long orderId) {
        view.showProgressBar();
        localRepository.getListQualityControlUploadWindow().subscribe(new Action1<List<QualityControlWindowModel>>() {
            @Override
            public void call(List<QualityControlWindowModel> qualityControlModels) {
                if (qualityControlModels.size() == positionList) {
                    GsonBuilder builder = new GsonBuilder();
                    builder.excludeFieldsWithoutExposeAnnotation();
                    Gson gson = builder.create();
                    String json = gson.toJson(qualityControlModels);
                    addLogQCUsecase.executeIO(new AddLogQCWindowUsecase.RequestValue(machineId,
                                    violator, qcId, orderId, UserManager.getInstance().getUser().getRole(),
                                    UserManager.getInstance().getUser().getId(), json),
                            new BaseUseCase.UseCaseCallback<AddLogQCWindowUsecase.ResponseValue, AddLogQCWindowUsecase.ErrorValue>() {
                                @Override
                                public void onSuccess(AddLogQCWindowUsecase.ResponseValue successResponse) {
                                    view.hideProgressBar();
                                    localRepository.updateStatusQCWindow().subscribe(new Action1<String>() {
                                        @Override
                                        public void call(String s) {
                                            view.showSuccess(CoreApplication.getInstance().getString(R.string.text_upload_success));
                                            view.refreshLayout();
                                            view.updateStateEditText(true);
                                        }
                                    });
                                }

                                @Override
                                public void onError(AddLogQCWindowUsecase.ErrorValue errorResponse) {
                                    view.hideProgressBar();
                                    view.showError(errorResponse.getDescription());
                                }
                            });
                } else {
                    positionList = 0;
                    positionImage = 0;
                    uploadImage(qualityControlModels, machineId, violator, qcId, orderId);
                }

            }
        });
    }

    @Override
    public void deleteAllQC() {
        localRepository.deleteAlLQC(UserManager.getInstance().getUser().getOrderType()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.refreshLayout();
                view.updateStateEditText(true);
            }
        });
    }

    public void showError(String error) {
        view.showError(error);
        view.startMusicError();
        view.turnOnVibrator();
    }

    public void saveBarcode(int machineId, String violator, int qcId, ProductWindowEntity productEntity) {
        localRepository.saveBarcodeQCWindow(machineId, violator, qcId, productEntity).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                view.refreshLayout();
                view.updateStateEditText(false);
                view.startMusicSuccess();
                view.turnOnVibrator();
            }
        });
    }

    public void uploadImage(List<QualityControlWindowModel> qualityControlModels, int machineId, String violator, int qcId, long orderId) {
        QualityControlWindowModel qualityControlModel = qualityControlModels.get(positionList);
        if (qualityControlModel.getImageList().size() > 0){
            ImageModel imageModel = qualityControlModel.getImageList().get(positionImage);
            //    File file = new File(imageModel.getPathFile());

            Bitmap bitmap = BitmapFactory.decodeFile(imageModel.getPathFile());
            bitmap = getResizedBitmap(bitmap, 800);
            File file = bitmapToFile(bitmap, imageModel.getPathFile());
            UserEntity userEntity = UserManager.getInstance().getUser();
            uploadImageUsecase.executeIO(new UploadImageUsecase.RequestValue(file, qualityControlModel.getOrderId(),
                            qualityControlModel.getDepartmentId(), file.getName(), userEntity.getId()),
                    new BaseUseCase.UseCaseCallback<UploadImageUsecase.ResponseValue, UploadImageUsecase.ErrorValue>() {
                        @Override
                        public void onSuccess(UploadImageUsecase.ResponseValue successResponse) {
                            localRepository.updateImageIdAndStatus(qualityControlModel.getId(), imageModel.getId(), successResponse.getImageId(),
                                    UserManager.getInstance().getUser().getOrderType())
                                    .subscribe(new Action1<String>() {
                                        @Override
                                        public void call(String s) {
                                            if (positionImage < qualityControlModel.getImageList().size() - 1) {
                                                positionImage++;
                                                uploadImage(qualityControlModels, machineId, violator, qcId, orderId);
                                            } else {
                                                positionImage = 0;
                                                positionList++;
                                                if (positionList <= qualityControlModels.size() - 1) {
                                                    uploadImage(qualityControlModels, machineId, violator, qcId, orderId);
                                                } else {
                                                    uploadData(machineId, violator, qcId, orderId);
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
        }else {
            positionImage = 0;
            positionList++;
            if (positionList <= qualityControlModels.size() - 1) {
                uploadImage(qualityControlModels, machineId, violator, qcId, orderId);
            } else {
                uploadData(machineId, violator, qcId, orderId);
            }
        }

    }

    public File bitmapToFile(Bitmap bmp, String filePath) {
        //create a file to write bitmap data
        File f = new File(filePath);
        try {
            f.createNewFile();
            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, /*ignored for PNG*/100, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;

    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
