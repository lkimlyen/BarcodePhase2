package com.demo.barcode.screen.detail_package;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.SocketRespone;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.LogCompleteCreatePack;
import com.demo.architect.data.model.offline.LogCompleteCreatePackList;
import com.demo.architect.data.model.offline.OrderModel;
import com.demo.architect.data.model.offline.PackageModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.repository.base.socket.ConnectSocket;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetInputForProductDetail;
import com.demo.architect.domain.GetDateServerUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.constants.Constants;
import com.demo.barcode.manager.ListProductManager;
import com.demo.barcode.manager.UserManager;
import com.demo.barcode.util.ConvertUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class DetailPackagePresenter implements DetailPackageContract.Presenter {

    private final String TAG = DetailPackagePresenter.class.getName();
    private final DetailPackageContract.View view;
    private final DeletePackageDetailUsecase deletePackageDetailUsecase;
    private final DeletePackageUsecase deletePackageUsecase;
    private final GetInputForProductDetail getInputForProductDetail;
    private final AddPackageACRbyJsonUsecase addPackageACRbyJsonUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    DetailPackagePresenter(@NonNull DetailPackageContract.View view, DeletePackageDetailUsecase deletePackageDetailUsecase,
                           DeletePackageUsecase deletePackageUsecase,
                           GetDateServerUsecase getDateServerUsecase,
                           GetInputForProductDetail getInputForProductDetail,
                           AddPackageACRbyJsonUsecase addPackageACRbyJsonUsecase) {
        this.view = view;
        this.deletePackageDetailUsecase = deletePackageDetailUsecase;
        this.deletePackageUsecase = deletePackageUsecase;
        this.getInputForProductDetail = getInputForProductDetail;
        this.addPackageACRbyJsonUsecase = addPackageACRbyJsonUsecase;
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
    public void getOrder(int orderId) {
        localRepository.findOrder(orderId).subscribe(new Action1<OrderModel>() {
            @Override
            public void call(OrderModel model) {
                view.showOrder(model);
            }
        });

        getProduct(orderId);

    }

    @Override
    public void getListHistory(int logId) {
        localRepository.findLogCreatePack(logId).subscribe(new Action1<LogCompleteCreatePackList>() {
            @Override
            public void call(LogCompleteCreatePackList logCompleteCreatePackProductModel) {
                view.showListCreatePack(logCompleteCreatePackProductModel);
            }
        });

        getDetailPack(logId);

    }

    public void getDetailPack(int logId) {
        localRepository.findLogCompletById(logId).subscribe(new Action1<LogCompleteCreatePackList>() {
            @Override
            public void call(LogCompleteCreatePackList logCompleteCreatePackList) {
                view.showDetailPack(logCompleteCreatePackList);
            }
        });
    }

    @Override
    public void deleteCode(int id, int productId, int logId, int serial, int number) {
        view.showProgressBar();
        int userId = UserManager.getInstance().getUser().getUserId();

        localRepository.checkStatus(id).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    deletePackageDetailUsecase.executeIO(new DeletePackageDetailUsecase.RequestValue(logId, productId, userId),
                            new BaseUseCase.UseCaseCallback<DeletePackageDetailUsecase.ResponseValue,
                                    DeletePackageDetailUsecase.ErrorValue>() {
                                @Override
                                public void onSuccess(DeletePackageDetailUsecase.ResponseValue successResponse) {
                                    view.hideProgressBar();
                                    // getProduct(orderId);
                                    localRepository.deleteLogComplete(id, logId).subscribe(new Action1<Integer>() {
                                        @Override
                                        public void call(Integer num) {
                                            view.showNumTotal(num);
                                            view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_success));
                                            ProductEntity productEntity = ListProductManager.getInstance().getProductBySerial(serial);
                                            productEntity.setNumScaned(productEntity.getNumScaned() - number);
                                            productEntity.setFull(false);
                                            ListProductManager.getInstance().updateEntity(productEntity);

                                        }
                                    });
                                }

                                @Override
                                public void onError(DeletePackageDetailUsecase.ErrorValue errorResponse) {
                                    view.hideProgressBar();
                                    view.showError(errorResponse.getDescription());
                                }
                            });
                } else {
                    localRepository.deleteLogComplete(id, logId).subscribe(new Action1<Integer>() {
                        @Override
                        public void call(Integer num) {
                            view.showNumTotal(num);
                            view.hideProgressBar();
                            view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_success));
                            ProductEntity productEntity = ListProductManager.getInstance().getProductBySerial(serial);
                            productEntity.setNumScaned(productEntity.getNumScaned() - number);
                            productEntity.setFull(false);
                            ListProductManager.getInstance().updateEntity(productEntity);
                        }
                    });
                }
            }
        });


    }

    @Override
    public void deletePack(int logId, int orderId) {
        view.showProgressBar();
        int userId = UserManager.getInstance().getUser().getUserId();
        deletePackageUsecase.executeIO(new DeletePackageUsecase.RequestValue(logId, userId),
                new BaseUseCase.UseCaseCallback<DeletePackageUsecase.ResponseValue,
                        DeletePackageUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(DeletePackageUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        localRepository.deletePack(logId, orderId).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                view.backToHistory(Constants.DELETE);
                            }
                        });
                    }

                    @Override
                    public void onError(DeletePackageUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });

    }

    @Override
    public void printStemp(int orderId, int serial, int serverId, int logId) {
        localRepository.findIPAddress().subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                if (address == null) {
                    view.showError(CoreApplication.getInstance().getString(R.string.text_no_ip_address));
                    return;
                }
                view.showProgressBar();
                ConnectSocket connectSocket = new ConnectSocket(address.getIpAddress(), address.getPortNumber(),
                        serverId, new ConnectSocket.onPostExecuteResult() {
                    @Override
                    public void onPostExecute(SocketRespone respone) {
                        if (respone.getConnect() == 1 && respone.getResult() == 1) {
                            if (serverId == 0) {
                                updateData(logId, orderId, serial, true);
                            } else {
                                view.hideProgressBar();
                                view.backToHistory(Constants.PRINT);
                            }
                        } else {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_connect_printer));
                            view.hideProgressBar();
                        }
                    }
                });

                connectSocket.execute();
            }
        });


    }

    private int count = 0;

    @Override
    public void updateData(int logId, int orderId, int serial, boolean print) {
        localRepository.logCompleteToJson(logId).subscribe(new Action1<List<LogCompleteCreatePack>>() {
            @Override
            public void call(List<LogCompleteCreatePack> list) {
                if (list != null) {
                    if (list.size() > 0) {
                        List<PackageModel> packageModels = new ArrayList<>();
                        for (LogCompleteCreatePack pack : list) {
                            PackageModel model = new PackageModel(pack.getOrderId(),
                                    serial, pack.getProductId(), pack.getBarcode(), pack.getNumInput(),
                                    pack.getLatitude(), pack.getLongitude(), pack.getDeviceTime(), pack.getCreateBy());
                            packageModels.add(model);
                        }
                        Gson gson = new Gson();
                        String json = gson.toJson(packageModels);
                        Log.d("PARSEARRAYTOJSON", json);
                        addPackageACRbyJsonUsecase.executeIO(new AddPackageACRbyJsonUsecase.RequestValue(json),
                                new BaseUseCase.UseCaseCallback<AddPackageACRbyJsonUsecase.ResponseValue,
                                        AddPackageACRbyJsonUsecase.ErrorValue>() {
                                    @Override
                                    public void onSuccess(AddPackageACRbyJsonUsecase.ResponseValue successResponse) {

                                        localRepository.updateStatusLog(logId).subscribe(new Action1<String>() {
                                            @Override
                                            public void call(String s) {
                                                localRepository.updateStatusAndNumberProduct(logId).subscribe(new Action1<String>() {
                                                    @Override
                                                    public void call(String s) {
                                                        if (print) {
                                                            printStemp(orderId, serial, successResponse.getId(), logId);
                                                        } else {
                                                            view.backToHistory(Constants.DONE);
                                                        }
                                                    }
                                                });

                                            }
                                        });

                                    }

                                    @Override
                                    public void onError(AddPackageACRbyJsonUsecase.ErrorValue errorResponse) {
                                        view.hideProgressBar();
                                        view.showError(errorResponse.getDescription());
                                    }
                                });

                    } else {
                        if (print) {
                            printStemp(orderId, serial, logId, logId);
                        } else {
                            view.showSuccess(CoreApplication.getInstance().getString(R.string.text_not_code_scan_new));
                        }
                    }
                } else {
                    if (print) {
                        printStemp(orderId, serial, logId, logId);
                    } else {
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_not_code_scan_new));
                    }
                }
            }

        });


    }

    private List<ProductModel> list;
    private OrderModel orderModel;

    @Override
    public void checkBarcode(String barcode, int orderId, int logId) {
        list = new ArrayList<>();
        orderModel = new OrderModel();
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

        localRepository.findOrder(orderId).subscribe(new Action1<OrderModel>() {
            @Override
            public void call(OrderModel model) {
                orderModel = model;
            }
        });

        List<ProductEntity> list = ListProductManager.getInstance().getListProduct();


        int checkBarcode = 0;

        for (ProductEntity model : list) {
            String barcodeMain = orderModel.getCodeProduction() + model.getStt();
            if (barcode.equals(barcodeMain)) {
                checkBarcode++;
                if (!model.isFull()) {
                    localRepository.checkExistCode(logId, barcode).subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (!aBoolean) {
                                view.showDialogNumber(model, barcode);
                            } else {
                                view.showError(CoreApplication.getInstance().getString(R.string.text_code_exist_in_pack));
                                view.startMusicError();
                                view.turnOnVibrator();
                            }
                        }
                    });

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

    @Override
    public int countListScan(int logId) {
        count = 0;
        localRepository.countCodeNotUp(logId).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                count = integer;
            }
        });
        return count;
    }

    @Override
    public void saveBarcode(double latitude, double longitude, String barcode, int logId, int numberInput, int serial) {
        view.showProgressBar();
        String deviceTime = ConvertUtils.getDateTimeCurrent();
        int userId = UserManager.getInstance().getUser().getUserId();
        String phone = Settings.Secure.getString(CoreApplication.getInstance().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        ProductEntity product = ListProductManager.getInstance().getProductBySerial(serial);
        ProductModel productModel = new ProductModel(product.getProductID(), orderModel.getId(),
                product.getCodeColor(), product.getStt(), product.getLength(), product.getWide(),
                product.getDeep(), product.getGrain(), product.getNumber(), product.getNumber() - product.getNumScaned(),
                product.getNumScaned(), product.getNumScaned());
        LogCompleteCreatePack model = new LogCompleteCreatePack(barcode, deviceTime, deviceTime,
                latitude, longitude, phone, 0, null, orderModel.getId(), 0,
                0, numberInput, Constants.WAITING_UPLOAD, userId);

        localRepository.addLogCompleteCreatePack(productModel, model, logId).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {

                view.hideProgressBar();
                view.showNumTotal(integer);
                view.startMusicSuccess();
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                view.turnOnVibrator();
                product.setNumScaned(product.getNumScaned() + 1);
                if (product.getNumber() - product.getNumScaned() == 0) {
                    product.setFull(true);
                }
                ListProductManager.getInstance().updateEntity(product);
            }
        });


    }

    @Override
    public void deleteCodeNotComplete(int logId) {
        localRepository.deleteCodeNotComplete(logId).subscribe();
    }

    public void getProduct(int orderId) {
        view.showProgressBar();
        getInputForProductDetail.executeIO(new GetInputForProductDetail.RequestValue(orderId),
                new BaseUseCase.UseCaseCallback<GetInputForProductDetail.ResponseValue,
                        GetInputForProductDetail.ErrorValue>() {
                    @Override
                    public void onSuccess(GetInputForProductDetail.ResponseValue successResponse) {
                        view.hideProgressBar();
//                        localRepository.deleteProduct().subscribe();
//                        //localRepository.updateStatusAndNumberProduct(orderId).subscribe();
//                        for (ProductEntity item : successResponse.getEntity()) {
//                            ProductModel model = new ProductModel(item.getProductID(), orderId, item.getCodeColor(),
//                                    item.getStt(), item.getLength(), item.getWide(), item.getDeep(), item.getGrain(),
//                                    item.getNumber(), item.getNumber(), 0, 0);
//                            localRepository.addProduct(model).subscribe();
//                        }
                        ListProductManager.getInstance().setListProduct(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetInputForProductDetail.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });

    }
}
