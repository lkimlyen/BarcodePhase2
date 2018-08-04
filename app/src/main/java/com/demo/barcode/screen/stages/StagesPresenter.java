package com.demo.barcode.screen.stages;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.OrderACREntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetAllDetailForSOACRUsecase;
import com.demo.architect.domain.GetAllSOACRUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.constants.Constants;
import com.demo.barcode.manager.ListOrderManager;
import com.demo.barcode.manager.ListProductManager;
import com.demo.barcode.manager.UserManager;
import com.demo.barcode.util.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class StagesPresenter implements StagesContract.Presenter {

    private final String TAG = StagesPresenter.class.getName();
    private final StagesContract.View view;
    private final GetAllSOACRUsecase getAllSOACRUsecase;
    private final GetAllDetailForSOACRUsecase getAllDetailForSOACRUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    StagesPresenter(@NonNull StagesContract.View view,
                    GetAllSOACRUsecase getAllSOACRUsecase,
                    GetAllDetailForSOACRUsecase getAllDetailForSOACRUsecase) {
        this.view = view;
        this.getAllSOACRUsecase = getAllSOACRUsecase;
        this.getAllDetailForSOACRUsecase = getAllDetailForSOACRUsecase;
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
    public void getData() {
        view.showProgressBar();
        getAllSOACRUsecase.executeIO(new GetAllSOACRUsecase.RequestValue(),
                new BaseUseCase.UseCaseCallback<GetAllSOACRUsecase.ResponseValue,
                        GetAllSOACRUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetAllSOACRUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        //localRepository.deleteAllOrder().subscribe();
                        List<OrderModel> list = new ArrayList<>();
                        list.add(new OrderModel(CoreApplication.getInstance().getString(R.string.text_choose_request_produce)));
                        int userId = UserManager.getInstance().getUser().getUserId();
                        for (OrderACREntity entity : successResponse.getEntity()) {
                            OrderModel model = new OrderModel(entity.getId(), entity.getCustomerID(), entity.getCode(), entity.getCodeSX(), entity.getCustomerName(), userId,
                                    ConvertUtils.getDateTimeCurrent());
//                            localRepository.addItemAsyns(model).subscribe();
                            list.add(model);
                        }
                        ListOrderManager.getInstance().setListOrder(list);
                        view.showRequestProduction(list);
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_download_list_prodution_success));

                    }

                    @Override
                    public void onError(GetAllSOACRUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });


    }

    @Override
    public void getRequestProduction() {
        localRepository.findAllOrder().subscribe(new Action1<List<OrderModel>>() {
            @Override
            public void call(List<OrderModel> orderModels) {
                List<OrderModel> list = new ArrayList<>();
                list.add(new OrderModel(CoreApplication.getInstance().getString(R.string.text_choose_request_produce)));
                list.addAll(orderModels);
                view.showRequestProduction(list);
            }
        });

    }

    @Override
    public void getProduct(int orderId) {
        view.showProgressBar();
        getAllDetailForSOACRUsecase.executeIO(new GetAllDetailForSOACRUsecase.RequestValue(orderId),
                new BaseUseCase.UseCaseCallback<GetAllDetailForSOACRUsecase.ResponseValue,
                        GetAllDetailForSOACRUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetAllDetailForSOACRUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        // localRepository.deleteProduct().subscribe();
                        if (successResponse.getEntity().size() == 0) {
                            view.showSuccess(CoreApplication.getInstance().getString(R.string.text_product_empty));

                        } else {
//                            List<ProductModel> list = new ArrayList<>();
//                            for (ProductEntity item : successResponse.getEntity()) {
//                                ProductModel model = new ProductModel(item.getProductID(), orderId, item.getCodeColor(),
//                                        item.getStt(), item.getLength(), item.getWide(), item.getDeep(), item.getGrain(),
//                                        item.getNumber(), item.getNumber(), 0, 0);
//                                //localRepository.addProduct(model).subscribe();
//                                list.add(model);
//                            }
                            ListProductManager.getInstance().setListProduct(successResponse.getEntity());
                            view.showSuccess(CoreApplication.getInstance().getString(R.string.text_download_list_product_success));
                        }
                        //localRepository.updateStatusAndNumberProduct(orderId).subscribe();

                    }

                    @Override
                    public void onError(GetAllDetailForSOACRUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });

    }

    @Override
    public void checkBarcode(String barcode, int orderId, double latitude, double longitude) {
        if (barcode.contains(CoreApplication.getInstance().getString(R.string.text_minus))) {
            view.showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_type));
            view.startMusicError();
            view.turnOnVibrator();
            return;
        }
        if (barcode.length() < 10 || barcode.length() > 13) {
            view.showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_lenght));
            view.startMusicError();
            view.turnOnVibrator();
            return;
        }
        OrderModel orderModel = ListOrderManager.getInstance().getOrderById(orderId);

        List<ProductEntity> list = ListProductManager.getInstance().getListProduct();


        if (list.size() == 0) {
            view.showError(CoreApplication.getInstance().getString(R.string.text_product_empty));
            view.startMusicError();
            view.turnOnVibrator();
            return;
        }

        int checkBarcode = 0;

        for (ProductEntity model : list) {
            String barcodeMain = orderModel.getCodeProduction() + model.getStt();
            if (barcode.equals(barcodeMain)) {
                checkBarcode++;
                if (!model.isFull()) {
                    if (countListScan(orderId) < 11) {
                        saveBarcode(latitude,
                                longitude, barcode, model, orderModel);
                    } else {
                        localRepository.checkExistBarcodeScanCreate(barcode)
                                .subscribe(new Action1<Boolean>() {
                                    @Override
                                    public void call(Boolean aBoolean) {
                                        if (aBoolean) {
                                            saveBarcode(latitude,
                                                    longitude, barcode, model, orderModel);
                                        } else {
                                            view.showError(CoreApplication.getInstance().getString(R.string.text_list_had_enough));
                                            view.startMusicError();
                                            view.turnOnVibrator();
                                        }
                                    }
                                });
                    }


                } else {
                    view.showError(CoreApplication.getInstance().getString(R.string.text_number_input_had_enough));
                    view.startMusicError();
                    view.turnOnVibrator();
                }

                return;
            }
        }

        if (checkBarcode == 0) {
            view.showError(CoreApplication.getInstance().getString(R.string.text_barcode_no_exist));
            view.startMusicError();
            view.turnOnVibrator();
        }
    }

    public void saveBarcode(double latitude, double longitude, String barcode, ProductEntity product, OrderModel orderModel) {
        view.showProgressBar();
        String deviceTime = ConvertUtils.getDateTimeCurrent();
        int userId = UserManager.getInstance().getUser().getUserId();
        String phone = Settings.Secure.getString(CoreApplication.getInstance().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        ProductModel productModel = new ProductModel(product.getProductID(), orderModel.getId(),
                product.getCodeColor(), product.getStt(), product.getLength(), product.getWide(),
                product.getDeep(), product.getGrain(), product.getNumber(), product.getNumber() - product.getNumScaned(),
                product.getNumScaned(), product.getNumScaned());
        LogScanCreatePack model = new LogScanCreatePack(barcode, deviceTime, deviceTime,
                latitude, longitude, phone, product.getProductID(), orderModel.getId(), product.getStt(),
                0, product.getNumber(), 1, productModel.getNumberRest(), Constants.WAITING_UPLOAD, -1, userId);


        localRepository.addLogScanCreatePack(productModel, orderModel, model, orderModel.getId(), barcode).subscribe(new Action1<String>() {
            @Override
            public void call(String String) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                view.startMusicSuccess();
                view.turnOnVibrator();
                product.setNumScaned(product.getNumScaned() + 1);
                if (product.getNumber() - product.getNumScaned() == 0) {
                    product.setFull(true);
                }
                ListProductManager.getInstance().updateEntity(product);

                view.hideProgressBar();
            }
        });


    }

    @Override
    public void getListCreateCode(int orderId) {
        localRepository.findAllLog(orderId).subscribe(new Action1<LogScanCreatePackList>() {
            @Override
            public void call(LogScanCreatePackList logScanCreatePackList) {
                view.showLogScanCreatePack(logScanCreatePackList);

            }
        });
    }

    @Override
    public void deleteItemLog(LogScanCreatePack item) {
        ProductEntity productEntity = ListProductManager.getInstance().getProductBySerial(item.getSerial());
        productEntity.setNumScaned(productEntity.getNumScaned() - item.getNumInput());
        productEntity.setFull(false);
        ListProductManager.getInstance().updateEntity(productEntity);
        localRepository.deleteLogScanItem(item.getId()).subscribe();
    }

    @Override
    public void updateNumberInput(int id, int number, int serial, int currentNumber) {
        ProductEntity productEntity = ListProductManager.getInstance().getProductBySerial(serial);
        productEntity.setNumScaned(productEntity.getNumScaned() + (number - currentNumber));
        if (productEntity.getNumber() - productEntity.getNumScaned() == 0) {
            productEntity.setFull(true);
        }
        localRepository.updateNumberLog(id, number).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                view.startMusicSuccess();
                view.turnOnVibrator();
            }
        });
    }

    @Override
    public void deleteAllItemLog() {
        localRepository.deleteAllLog().subscribe();
    }

    private int count = 0;

    @Override
    public int countListScan(int orderId) {
        count = 0;
        localRepository.findAllLog(orderId).subscribe(new Action1<LogScanCreatePackList>() {
            @Override
            public void call(LogScanCreatePackList logScanCreatePackList) {
                if (logScanCreatePackList != null) {
                    count = logScanCreatePackList.getItemList().size();
                }
            }
        });

        return count;
    }

}
