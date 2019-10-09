package com.demo.barcode.screen.warehousing_wd;

import android.provider.Settings;
import androidx.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.ProductWarehouseEntity;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.ProductWarehouseModel;
import com.demo.architect.data.model.offline.WarehousingModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetInputForProductWarehouseUsecase;
import com.demo.architect.domain.GetListSOWarehouseUsecase;
import com.demo.architect.domain.ScanWarehousingUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.constants.Constants;
import com.demo.barcode.manager.ListProductWarehouseManager;
import com.demo.barcode.manager.ListSOManager;
import com.demo.barcode.manager.UserManager;
import com.demo.barcode.util.ConvertUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class WarehousingWDPresenter implements WarehousingWDContract.Presenter {

    private final String TAG = WarehousingWDPresenter.class.getName();
    private final WarehousingWDContract.View view;
    private final GetInputForProductWarehouseUsecase getInputForProductDetail;
    private final GetListSOWarehouseUsecase getListSOUsecase;
    private final ScanWarehousingUsecase scanWarehousingUsecase;

    @Inject
    LocalRepository localRepository;

    @Inject
    WarehousingWDPresenter(@NonNull WarehousingWDContract.View view,
                           GetInputForProductWarehouseUsecase getInputForProductDetail, GetListSOWarehouseUsecase getListSOUsecase,
                           ScanWarehousingUsecase scanWarehousingUsecase) {
        this.view = view;
        this.getInputForProductDetail = getInputForProductDetail;
        this.getListSOUsecase = getListSOUsecase;
        this.scanWarehousingUsecase = scanWarehousingUsecase;
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
    public void checkBarcode(long orderId, String barcode) {
        //fomart barcode và kiểm tra mã barcode
        barcode = barcode.toUpperCase();
        if (!barcode.contains("-")) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_type));
            return;
        }
        //lấy sl gói nằm sau cũng của mã barcode
        String barcodeSplit = barcode.substring(0, barcode.lastIndexOf("-"));
        int numberPack = Integer.parseInt(barcode.substring(barcode.lastIndexOf("-") + 1).trim());

        List<ProductWarehouseEntity> list = ListProductWarehouseManager.getInstance().getListProduct();
        if (list.size() == 0) {
            showError(CoreApplication.getInstance().getString(R.string.text_product_empty));
            return;
        }
        ProductWarehouseEntity item = ListProductWarehouseManager.getInstance().getProductByBarcode(barcodeSplit);
        if (item != null) {
            String finalBarcode = barcode;
            //lấy sản phẩm nhập kho theo ProductWarehouseEntity
            localRepository.getProductWarehouse(item).subscribe(new Action1<ProductWarehouseModel>() {
                @Override
                public void call(ProductWarehouseModel productDetail) {

                    if (productDetail != null) {
                        //kiểm tra số lượng còn lại của product
                        if (productDetail.getNumberRest() > 0) {
                            saveBarcodeToDataBase(orderId, finalBarcode, productDetail, 1, numberPack, false);
                        } else {
                            saveBarcodeToDataBase(orderId, finalBarcode, productDetail, 1, numberPack, true);
                        }
                    }
                }
            });

        } else {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_no_exist));
        }
    }


    public void showError(String error) {
        view.showError(error);
        view.startMusicError();
        view.turnOnVibrator();
    }

    @Override
    public void deleteItem(long id) {
        view.showProgressBar();
        localRepository.deleteWarehousing(id).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.hideProgressBar();
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_success));
            }
        });
    }


    @Override
    public void uploadData(long orderId) {
        view.showProgressBar();
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        UserEntity user = UserManager.getInstance().getUser();
        String phone = Settings.Secure.getString(CoreApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        localRepository.getListWarehousingWindowUpload().subscribe(new Action1<List<WarehousingModel>>() {
            @Override
            public void call(List<WarehousingModel> list) {
                scanWarehousingUsecase.executeIO(new ScanWarehousingUsecase.RequestValue(user.getId(), orderId, phone, ConvertUtils.getDateTimeCurrent(), gson.toJson(list)),
                        new BaseUseCase.UseCaseCallback<ScanWarehousingUsecase.ResponseValue,
                                ScanWarehousingUsecase.ErrorValue>() {
                            @Override
                            public void onSuccess(ScanWarehousingUsecase.ResponseValue successResponse) {
                                view.hideProgressBar();

                                localRepository.updateStatusWarehousing().subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_upload_success));
                                        getListProduct(orderId, true);

                                    }
                                });
                            }

                            @Override
                            public void onError(ScanWarehousingUsecase.ErrorValue errorResponse) {
                                view.hideProgressBar();
                                view.showError(errorResponse.getDescription());
                            }
                        });
            }
        });

    }

    @Override
    public void saveBarcodeToDataBase(long orderId, String barcode, ProductWarehouseModel
            productDetail, int number, int numberPack, boolean residual) {
        view.showProgressBar();
        UserEntity user = UserManager.getInstance().getUser();
        WarehousingModel model = new WarehousingModel(orderId, productDetail.getProductId(), barcode, productDetail.getPack(),
                number * numberPack, number, numberPack, ConvertUtils.getDateTimeCurrent(), 0, 0, Constants.WAITING_UPLOAD, user.getId());
        localRepository.warehousing(model).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if (!residual) {
                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                    view.startMusicSuccess();
                } else {
                    view.showCheckResidual();
                }
                view.turnOnVibrator();
                view.hideProgressBar();

            }
        });

    }

    @Override
    public void updateNumberScan(long id, int number, boolean update) {
        view.showProgressBar();
        localRepository.updateNumberWarehousing(id, number).subscribe(new Action1<String>() {
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
    public void deleteAllData() {

        localRepository.deleteAllWarehousing().subscribe();
    }

    @Override
    public void getAllListScanWarehousing() {
        //xóa tất cả dữ liệu chưa upload lên trc khi lấy ds đã quét
        localRepository.deleteAllWarehousing().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                localRepository.getAllListWarehousing().subscribe(new Action1<RealmResults<WarehousingModel>>() {
                    @Override
                    public void call(RealmResults<WarehousingModel> results) {
                        view.showListWarehousing(results);
                    }
                });
            }
        });

    }


    @Override
    public void getListSO() {
        view.showProgressBar();
        getListSOUsecase.executeIO(new GetListSOWarehouseUsecase.RequestValue(UserManager.getInstance().getUser().getOrderType()),
                new BaseUseCase.UseCaseCallback<GetListSOWarehouseUsecase.ResponseValue,
                        GetListSOWarehouseUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListSOWarehouseUsecase.ResponseValue successResponse) {
                        view.showListSO(successResponse.getEntity());
                        view.hideProgressBar();
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_so_success));

                    }

                    @Override
                    public void onError(GetListSOWarehouseUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListSOManager.getInstance().setListSO(new ArrayList<>());

                    }
                });
    }


    @Override
    public void getListProduct(long orderId, boolean refresh) {
        view.showProgressBar();
        UserEntity user = UserManager.getInstance().getUser();
        getInputForProductDetail.executeIO(new GetInputForProductWarehouseUsecase.RequestValue(orderId),
                new BaseUseCase.UseCaseCallback<GetInputForProductWarehouseUsecase.ResponseValue,
                        GetInputForProductWarehouseUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetInputForProductWarehouseUsecase.ResponseValue successResponse) {

                        ListProductWarehouseManager.getInstance().setListProduct(successResponse.getEntity());
                        if (!refresh) {
                            view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_list_detail_success));
                        }
                        view.hideProgressBar();
                    }

                    @Override
                    public void onError(GetInputForProductWarehouseUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        ListProductWarehouseManager.getInstance().setListProduct(new ArrayList<>());
                        view.showError(errorResponse.getDescription());
                    }
                });

    }

}
