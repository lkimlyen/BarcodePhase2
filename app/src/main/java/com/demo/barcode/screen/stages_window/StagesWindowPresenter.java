package com.demo.barcode.screen.stages_window;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.ProductWindowEntity;
import com.demo.architect.data.model.SocketRespone;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.LogScanStagesWindowModel;
import com.demo.architect.data.model.offline.ProductDetailWindowModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.repository.base.socket.ConnectSocketDelivery;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetInputForProductDetailWindowUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.ScanProductDetailOutWindowUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListDepartmentManager;
import com.demo.barcode.manager.ListProductWindowManager;
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

public class StagesWindowPresenter implements StagesWindowContract.Presenter {

    private final String TAG = StagesWindowPresenter.class.getName();
    private final StagesWindowContract.View view;
    private final GetInputForProductDetailWindowUsecase getInputForProductDetail;
    private final GetListSOUsecase getListSOUsecase;
    private final ScanProductDetailOutWindowUsecase scanProductDetailOutUsecase;

    @Inject
    LocalRepository localRepository;

    @Inject
    StagesWindowPresenter(@NonNull StagesWindowContract.View view,
                          GetInputForProductDetailWindowUsecase getInputForProductDetail, GetListSOUsecase getListSOUsecase,
                          ScanProductDetailOutWindowUsecase scanProductDetailOutUsecase) {
        this.view = view;
        this.getInputForProductDetail = getInputForProductDetail;
        this.getListSOUsecase = getListSOUsecase;
        this.scanProductDetailOutUsecase = scanProductDetailOutUsecase;
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
    public void checkBarcode(String barcode, int departmentId) {
        if (barcode.contains(CoreApplication.getInstance().getString(R.string.text_minus))) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_type));
            return;
        }
        List<ProductWindowEntity> list = ListProductWindowManager.getInstance().getListProduct();

        if (list.size() == 0) {
            showError(CoreApplication.getInstance().getString(R.string.text_product_empty));
            return;
        }

        ProductWindowEntity model = ListProductWindowManager.getInstance().getProductByBarcode(barcode);
        if (model != null) {
            // if (model.getListDepartmentID().contains(departmentId)) {
            localRepository.getProductDetailWindow(model).subscribe(new Action1<ProductDetailWindowModel>() {
                @Override
                public void call(ProductDetailWindowModel productDetail) {
                    if (productDetail != null) {
                        if (productDetail.getNumberRest() > 0) {
                            saveBarcodeToDataBase(productDetail, 1, departmentId, false);
                        } else {
                            saveBarcodeToDataBase(productDetail, 1, departmentId, true);
                        }
                    } else {
                        showError(CoreApplication.getInstance().getString(R.string.text_product_not_in_times));
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

    private int count = 0;


    @Override
    public void deleteScanStages(long stagesId) {
        view.showProgressBar();
        localRepository.deleteScanStagesWindow(stagesId).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.hideProgressBar();
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_success));
                view.refreshLayout();
            }
        });
    }


    @Override
    public void uploadData(long orderId, int departmentIn) {
        view.showProgressBar();
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        UserEntity user = UserManager.getInstance().getUser();
        localRepository.getListLogScanStagesWindowUpload().subscribe(new Action1<List<LogScanStagesWindowModel>>() {
            @Override
            public void call(List<LogScanStagesWindowModel> list) {
                if (list.size() == 0) {
                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_no_data_upload));
                    return;
                }
                scanProductDetailOutUsecase.executeIO(new ScanProductDetailOutWindowUsecase.RequestValue(
                        orderId,user.getRole(),departmentIn,user.getId(),gson.toJson(list)),
                        new BaseUseCase.UseCaseCallback<ScanProductDetailOutWindowUsecase.ResponseValue,
                                ScanProductDetailOutWindowUsecase.ErrorValue>() {
                            @Override
                            public void onSuccess(ScanProductDetailOutWindowUsecase.ResponseValue successResponse) {
                                view.hideProgressBar();

                                localRepository.deleteAllScanStagesWindow().subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_upload_success));
                                        view.showPrintDeliveryNote(successResponse.getId());
                                        getListProduct(orderId, true);


                                    }
                                });
                            }

                            @Override
                            public void onError(ScanProductDetailOutWindowUsecase.ErrorValue errorResponse) {
                                view.hideProgressBar();
                                view.showError(errorResponse.getDescription());
                            }
                        });
            }
        });

    }

    @Override
    public void saveBarcodeToDataBase(ProductDetailWindowModel
                                              productDetail, int number, int departmentId, boolean residual) {
        view.showProgressBar();
        UserEntity user = UserManager.getInstance().getUser();

        LogScanStagesWindowModel logScanStages = new LogScanStagesWindowModel(productDetail.getOrderId(), departmentId, user.getRole(), productDetail.getProductSetDetailID(),
                productDetail.getBarcode(), number, ConvertUtils.getDateTimeCurrent(), user.getId());
        localRepository.addLogScanStagesWindow(logScanStages).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if (!residual) {
                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                    view.startMusicSuccess();
                }else {
                    view.showCheckResidual();
                }
                view.turnOnVibrator();
                view.hideProgressBar();
                view.refreshLayout();

            }
        });

    }

    @Override
    public void updateNumberScan(long stagesId, int number, boolean update) {
        view.showProgressBar();
        localRepository.updateNumberScanStagesWindow(stagesId, number).subscribe(new Action1<String>() {
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
    public void print(int id, int idPrint) {
        localRepository.findIPAddress().subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                if (address == null) {
                    //view.showError(CoreApplication.getInstance().getString(R.string.text_no_ip_address));
                    view.showDialogCreateIPAddress(idPrint);
                    view.hideProgressBar();
                    return;
                }
                view.showProgressBar();
                ConnectSocketDelivery connectSocket = new ConnectSocketDelivery(address.getIpAddress(), address.getPortNumber(),
                        id, new ConnectSocketDelivery.onPostExecuteResult() {
                    @Override
                    public void onPostExecute(SocketRespone respone) {
                        if (respone.getConnect() == 1 && respone.getResult() == 1) {

                            if (id == -1) {
                                print(idPrint, idPrint);


                            } else {
                                view.hideProgressBar();
                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_print_success));


                            }
                        } else {
                            view.hideProgressBar();
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_connect_printer));

                        }
                    }
                });

                connectSocket.execute();
            }
        });
    }

    @Override
    public void saveIPAddress(String ipAddress, int port, int idPrint) {
        long userId = UserManager.getInstance().getUser().getId();
        IPAddress model = new IPAddress(1, ipAddress, port, userId, ConvertUtils.getDateTimeCurrent());
        localRepository.insertOrUpdateIpAddress(model).subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                //  view.showIPAddress(address);
                print(-1, idPrint);
            }
        });
    }

    @Override
    public void deleteAllData() {

        localRepository.deleteAllScanStagesWindow().subscribe();
    }

    @Override
    public void getAllListStages() {
        localRepository.deleteAllScanStagesWindow().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                localRepository.getAllListStagesWindow().subscribe(new Action1<RealmResults<LogScanStagesWindowModel>>() {
                    @Override
                    public void call(RealmResults<LogScanStagesWindowModel> results) {
                        view.showListLogScanStages(results);
                    }
                });
            }
        });

    }


    @Override
    public void getListDepartment() {

        view.showListDepartment(ListDepartmentManager.getInstance().getListDepartmentWithOrderType(
                UserManager.getInstance().getUser().getRole(), UserManager.getInstance().getUser().getOrderType()
        ));
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
    public void getListProduct(long orderId, boolean refresh) {
        view.showProgressBar();
        UserEntity user = UserManager.getInstance().getUser();
        getInputForProductDetail.executeIO(new GetInputForProductDetailWindowUsecase.RequestValue(orderId, user.getRole()),
                new BaseUseCase.UseCaseCallback<GetInputForProductDetailWindowUsecase.ResponseValue,
                        GetInputForProductDetailWindowUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetInputForProductDetailWindowUsecase.ResponseValue successResponse) {

                        ListProductWindowManager.getInstance().setListProduct(successResponse.getEntity());
                        if (!refresh) {
                            view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_list_detail_success));
                        }
                        view.hideProgressBar();

                    }

                    @Override
                    public void onError(GetInputForProductDetailWindowUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListProductWindowManager.getInstance().setListProduct(new ArrayList<>());
                    }
                });

    }

}
