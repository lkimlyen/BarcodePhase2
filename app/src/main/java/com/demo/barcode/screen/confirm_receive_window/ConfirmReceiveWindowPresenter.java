package com.demo.barcode.screen.confirm_receive_window;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.DeliveryEntity;
import com.demo.architect.data.model.PrintConfirmEntity;
import com.demo.architect.data.model.SocketRespone;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.DeliveryNoteWindowModel;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.LogScanConfirmWindowModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.repository.base.socket.ConnectSocketConfirm;
import com.demo.architect.domain.AddPhieuGiaoNhanUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.ConfirmInputWindowUsecase;
import com.demo.architect.domain.GetDetailInByDeliveryWindowUsecase;
import com.demo.architect.domain.GetListMaPhieuGiaoUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListDeliveryNoteManager;
import com.demo.barcode.manager.ListDepartmentManager;
import com.demo.barcode.manager.ListOrderConfirmWindowManager;
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

public class ConfirmReceiveWindowPresenter implements ConfirmReceiveWindowContract.Presenter {

    private final String TAG = ConfirmReceiveWindowPresenter.class.getName();
    private final ConfirmReceiveWindowContract.View view;
    private final GetListSOUsecase getListSOUsecase;
    private final ConfirmInputWindowUsecase confirmInputUsecase;
    private final AddPhieuGiaoNhanUsecase addPhieuGiaoNhanUsecase;
    private final GetListMaPhieuGiaoUsecase getListMaPhieuGiaoUsecase;
    private final GetDetailInByDeliveryWindowUsecase getListInputUnConfirmByMaPhieuUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    ConfirmReceiveWindowPresenter(@NonNull ConfirmReceiveWindowContract.View view, GetListSOUsecase getListSOUsecase,
                                  ConfirmInputWindowUsecase confirmInputUsecase, AddPhieuGiaoNhanUsecase addPhieuGiaoNhanUsecase,
                                  GetListMaPhieuGiaoUsecase getListMaPhieuGiaoUsecase,
                                  GetDetailInByDeliveryWindowUsecase getListInputUnConfirmByMaPhieuUsecase) {
        this.view = view;
        this.getListSOUsecase = getListSOUsecase;
        this.confirmInputUsecase = confirmInputUsecase;
        this.addPhieuGiaoNhanUsecase = addPhieuGiaoNhanUsecase;
        this.getListMaPhieuGiaoUsecase = getListMaPhieuGiaoUsecase;
        this.getListInputUnConfirmByMaPhieuUsecase = getListInputUnConfirmByMaPhieuUsecase;
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
                        view.hideProgressBar();
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_so_success));
                    }

                    @Override
                    public void onError(GetListSOUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showErrorByType(errorResponse.getDescription(),1);
                        ListSOManager.getInstance().setListSO(new ArrayList<>());


                    }
                });
    }


    @Override
    public void getListConfirm(long maPhieuId, boolean refresh) {
        view.showProgressBar();
        getListInputUnConfirmByMaPhieuUsecase.executeIO(new GetDetailInByDeliveryWindowUsecase.RequestValue(maPhieuId),
                new BaseUseCase.UseCaseCallback<GetDetailInByDeliveryWindowUsecase.ResponseValue,
                        GetDetailInByDeliveryWindowUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetDetailInByDeliveryWindowUsecase.ResponseValue successResponse) {
                        ListOrderConfirmWindowManager.getInstance().setListOrder(successResponse.getEntity());
                        localRepository.addOrderConfirmWindow(successResponse.getEntity()).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                if (!refresh) {
                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_list_confirm_success));
                                }
                                getListConfirm();
                                view.hideProgressBar();

                            }
                        });
                    }

                    @Override
                    public void onError(GetDetailInByDeliveryWindowUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        if (!refresh) {
                            view.showErrorByType(errorResponse.getDescription(),3);
                            ListOrderConfirmWindowManager.getInstance().setListOrder(new ArrayList<>());

                        }


                    }
                });
    }

    @Override
    public void getListDeliveryCode(long orderId, int departmentIdOut) {
        view.showProgressBar();
        UserEntity user = UserManager.getInstance().getUser();
        getListMaPhieuGiaoUsecase.executeIO(new GetListMaPhieuGiaoUsecase.RequestValue(orderId, user.getRole(), departmentIdOut),
                new BaseUseCase.UseCaseCallback<GetListMaPhieuGiaoUsecase.ResponseValue, GetListMaPhieuGiaoUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListMaPhieuGiaoUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        view.showListDeliveryCode(successResponse.getEntity());
                        ListDeliveryNoteManager.getInstance().setListDeliveryNote(successResponse.getEntity());

                    }

                    @Override
                    public void onError(GetListMaPhieuGiaoUsecase.ErrorValue errorResponse) {
                        view.showErrorByType(errorResponse.getDescription(),3);
                        ListDeliveryNoteManager.getInstance().setListDeliveryNote(new ArrayList<>());
                        view.hideProgressBar();
                    }
                });
    }

    @Override
    public void getListDepartment() {
        view.showListDepartment(ListDepartmentManager.getInstance().getListDepartmentWithOrderType(
                UserManager.getInstance().getUser().getRole(),UserManager.getInstance().getUser().getOrderType()
        ));
    }

    @Override
    public void getListConfirm() {
        localRepository.getListConfirmWindow().subscribe(new Action1<RealmResults<LogScanConfirmWindowModel>>() {
            @Override
            public void call(RealmResults<LogScanConfirmWindowModel> logScanConfirms) {
                view.showListConfirm(logScanConfirms);
            }
        });

    }



    @Override
    public void checkBarcode(String barcode) {
        if (barcode.contains(CoreApplication.getInstance().getString(R.string.text_minus))) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_type));
            return;
        }
        localRepository.findConfirmByBarcodeInWindow(barcode).subscribe(new Action1<LogScanConfirmWindowModel>() {
            @Override
            public void call(LogScanConfirmWindowModel logScanConfirm) {
                if (logScanConfirm == null) {
                    showError(CoreApplication.getInstance().getString(R.string.text_barcode_no_exist));
                } else {

                    if (logScanConfirm.isState()){
                        showError(CoreApplication.getInstance().getString(R.string.text_detail_confirm_complete));
                    }else {
                        DeliveryNoteWindowModel deliveryModel = logScanConfirm.getDeliveryNoteModel();
                        if (deliveryModel.getNumberRest() == 0) {
                            showError(CoreApplication.getInstance().getString(R.string.text_number_confirm_residual));
                        } else {
                            saveConfirm(logScanConfirm.getOutputId(), 1);
                        }
                    }



                }

            }
        });


    }

    @Override
    public void updateNumberConfirm(long outputId, int numberScan) {
        localRepository.updateNumnberLogConfirmWindow(outputId, numberScan, false).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                view.startMusicSuccess();
                view.turnOnVibrator();
                view.hideProgressBar();
            }
        });
    }

    @Override
    public void uploadData(long maPhieuId, long orderId, int departmentIdOut, int times, boolean checkPrint) {
        //    view.showError(CoreApplication.getInstance().getString(R.string.text_not_print_before_upload));
        view.showProgressBar();
        localRepository.getListLogScanConfirmWindow().subscribe(new Action1<List<LogScanConfirmWindowModel>>() {
            @Override
            public void call(List<LogScanConfirmWindowModel> logScanConfirms) {
                if (logScanConfirms.size() == 0) {
                    view.hideProgressBar();
                    view.showError(CoreApplication.getInstance().getString(R.string.text_no_data_upload_in_times_scan));
                    return;
                }
//                List<LogScanConfirmWindowModel> listUpload = new ArrayList<>();
//                for (LogScanConfirmWindowModel logScanConfirm : logScanConfirms) {
//                    if (logScanConfirm.getStatusConfirm() != -1) {
//                        listUpload.add(logScanConfirm);
//                    }
//                }

                GsonBuilder builder = new GsonBuilder();
                builder.excludeFieldsWithoutExposeAnnotation();
                Gson gson = builder.create();
                String json = gson.toJson(logScanConfirms);

//                List<PrintConfirmEntity> list = new ArrayList<>();
//                for (LogScanConfirmWindowModel logScanConfirm : logScanConfirms) {
//                    PrintConfirmEntity detailItem = new PrintConfirmEntity(logScanConfirm.getDeliveryNoteModel().getProductSetId(),
//                            logScanConfirm.getProductSetDetailId(), logScanConfirm.getNumberOut(),
//                            logScanConfirm.getDeliveryNoteModel().getNumberUsed(), logScanConfirm.getNumberConfirmed());
//
//                    list.add(detailItem);
//                }
//
//                String maPhieu1 = ListDeliveryNoteManager.getInstance().getDeliveryNoteById(maPhieuId);
//                DeliveryEntity deliveryEntity = new DeliveryEntity(maPhieu1, list);
//                String jsonWithData = new Gson().toJson(deliveryEntity);
                confirmInputUsecase.executeIO(new ConfirmInputWindowUsecase.RequestValue(UserManager.getInstance().getUser().getRole(),
                        UserManager.getInstance().getUser().getId(),
                        json), new BaseUseCase.UseCaseCallback<ConfirmInputWindowUsecase.ResponseValue,
                        ConfirmInputWindowUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(ConfirmInputWindowUsecase.ResponseValue successResponse) {
                        localRepository.updateStatusLogConfirmWindow().subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
//                                addPhieuGiaoNhanUsecase.executeIO(new AddPhieuGiaoNhanUsecase.RequestValue(orderId, UserManager.getInstance().getUser().getRole(),
//                                        departmentIdOut, times, jsonWithData, UserManager.getInstance().getUser().getId()
//                                ), new BaseUseCase.UseCaseCallback<AddPhieuGiaoNhanUsecase.ResponseValue,
//                                        AddPhieuGiaoNhanUsecase.ErrorValue>() {
//                                    @Override
//                                    public void onSuccess(AddPhieuGiaoNhanUsecase.ResponseValue successResponse) {
                                        view.hideProgressBar();
                                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_upload_success));
                                        getListConfirm(maPhieuId, true);
//                                    }
//
//                                    @Override
//                                    public void onError(AddPhieuGiaoNhanUsecase.ErrorValue errorResponse) {
//                                        view.showError(errorResponse.getDescription());
//                                        view.hideProgressBar();
//                                    }
//                                });
                               // Log.d(TAG, "Print: " + jsonWithData);

                            }
                        });

                    }

                    @Override
                    public void onError(ConfirmInputWindowUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
            }
        });
    }


    @Override
    public void saveIPAddress(String ipAddress, int port, long maPhieuId, String maPhieu, long orderId, int departmentIdOut, int times, long serverId, boolean upload) {
        long userId = UserManager.getInstance().getUser().getId();
        IPAddress model = new IPAddress(1, ipAddress, port, userId, ConvertUtils.getDateTimeCurrent());
        localRepository.insertOrUpdateIpAddress(model).subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                //  view.showIPAddress(address);
                print(maPhieuId, orderId, departmentIdOut, times, serverId, upload);
            }
        });
    }

    @Override
    public void confirmAll() {
        localRepository.confirmAllProductReceiveWindow().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.setStatusPrint(false);
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_confirm_all_success));
                view.startMusicSuccess();
                view.turnOnVibrator();
                view.hideProgressBar();
            }
        });
    }

    @Override
    public void cancelConfirmAll() {
        localRepository.cancelConfirmAllProductReceiveWindow().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.setStatusPrint(false);
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_cancle_confirm_all_success));
                view.startMusicSuccess();
                view.turnOnVibrator();
                view.hideProgressBar();
            }
        });
    }


    @Override
    public void print(long maPhieuId, long orderId, int departmentIdOut, int times, long serverId, boolean upload) {
        view.showProgressBar();
        UserEntity user = UserManager.getInstance().getUser();
        localRepository.findIPAddress().subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                if (address == null) {
                    //view.showError(CoreApplication.getInstance().getString(R.string.text_no_ip_address));
                    view.showDialogCreateIPAddress(upload);
                    view.hideProgressBar();
                    return;
                }
                ConnectSocketConfirm connectSocket = new ConnectSocketConfirm(address.getIpAddress(), address.getPortNumber(),
                        serverId, new ConnectSocketConfirm.onPostExecuteResult() {
                    @Override
                    public void onPostExecute(SocketRespone respone) {
                        if (respone.getConnect() == 1 && respone.getResult() == 1) {

                            if (serverId == -1) {
                                localRepository.getListLogScanConfirmWindow().subscribe(new Action1<List<LogScanConfirmWindowModel>>() {
                                    @Override
                                    public void call(List<LogScanConfirmWindowModel> logScanConfirms) {

                                        if (logScanConfirms.size() == 0) {
                                            view.hideProgressBar();
                                            view.showError(CoreApplication.getInstance().getString(R.string.text_not_data_print));
                                        } else {
                                            List<PrintConfirmEntity> list = new ArrayList<>();
                                            for (LogScanConfirmWindowModel logScanConfirm : logScanConfirms) {
                                                PrintConfirmEntity detailItem = new PrintConfirmEntity(logScanConfirm.getDeliveryNoteModel().getProductSetId(),
                                                        logScanConfirm.getProductSetDetailId(), (int) logScanConfirm.getNumberOut(),
                                                        (int) logScanConfirm.getDeliveryNoteModel().getNumberUsed(), (int) logScanConfirm.getNumberConfirmed());

                                                list.add(detailItem);
                                            }

                                            String maPhieu1 = ListDeliveryNoteManager.getInstance().getDeliveryNoteById(maPhieuId);
                                            DeliveryEntity deliveryEntity = new DeliveryEntity(maPhieu1, list);
                                            String jsonWithData = new Gson().toJson(deliveryEntity);
                                            addPhieuGiaoNhanUsecase.executeIO(new AddPhieuGiaoNhanUsecase.RequestValue(orderId, user.getRole(),
                                                    departmentIdOut, times, jsonWithData, user.getId()
                                            ), new BaseUseCase.UseCaseCallback<AddPhieuGiaoNhanUsecase.ResponseValue,
                                                    AddPhieuGiaoNhanUsecase.ErrorValue>() {
                                                @Override
                                                public void onSuccess(AddPhieuGiaoNhanUsecase.ResponseValue successResponse) {
                                                    print(maPhieuId, orderId, departmentIdOut, times, successResponse.getId(), upload);
                                                }

                                                @Override
                                                public void onError(AddPhieuGiaoNhanUsecase.ErrorValue errorResponse) {
                                                    view.showError(errorResponse.getDescription());
                                                    view.hideProgressBar();
                                                }
                                            });
                                            Log.d(TAG, "Print: " + jsonWithData);

                                        }
                                    }
                                });

                            } else {


                                view.setStatusPrint(true);
                                view.hideProgressBar();
                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_print_success));

                                if (upload) {
                                    uploadData(maPhieuId, orderId, departmentIdOut, times, true);
                                }


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


    public void saveConfirm(long outputId,
                            int number) {
        localRepository.updateNumnberLogConfirmWindow(outputId, number, true).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

                view.setStatusPrint(false);
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_update_barcode_success));
                view.startMusicSuccess();
                view.turnOnVibrator();
                view.hideProgressBar();
            }
        });
    }


    public void showError(String error) {
        view.showError(error);
        view.startMusicError();
        view.turnOnVibrator();
    }

}
