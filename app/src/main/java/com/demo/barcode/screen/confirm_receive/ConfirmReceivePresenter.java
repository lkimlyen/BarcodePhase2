package com.demo.barcode.screen.confirm_receive;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.DeliveryEntity;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.PrintConfirmEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.SocketRespone;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.LogScanConfirmModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.repository.base.socket.ConnectSocketConfirm;
import com.demo.architect.domain.AddPhieuGiaoNhanUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.ConfirmInputUsecase;
import com.demo.architect.domain.GetInputUnConfirmedUsecase;
import com.demo.architect.domain.GetListInputUnConfirmByMaPhieuUsecase;
import com.demo.architect.domain.GetListMaPhieuGiaoUsecase;
import com.demo.architect.domain.GetListProductDetailGroupUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.GetTimesInputAndOutputByDepartmentUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.constants.Constants;
import com.demo.barcode.manager.ListDeliveryNoteManager;
import com.demo.barcode.manager.ListDepartmentManager;
import com.demo.barcode.manager.ListOrderConfirmManager;
import com.demo.barcode.manager.ListGroupManager;
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

public class ConfirmReceivePresenter implements ConfirmReceiveContract.Presenter {

    private final String TAG = ConfirmReceivePresenter.class.getName();
    private final ConfirmReceiveContract.View view;
    private final GetListSOUsecase getListSOUsecase;
    private final ConfirmInputUsecase confirmInputUsecase;
    private final GetListProductDetailGroupUsecase getListProductDetailGroupUsecase;
    private final GetTimesInputAndOutputByDepartmentUsecase getTimesInputAndOutputByDepartmentUsecase;
    private final AddPhieuGiaoNhanUsecase addPhieuGiaoNhanUsecase;
    private final GetListMaPhieuGiaoUsecase getListMaPhieuGiaoUsecase;
    private final GetListInputUnConfirmByMaPhieuUsecase getListInputUnConfirmByMaPhieuUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    ConfirmReceivePresenter(@NonNull ConfirmReceiveContract.View view, GetListSOUsecase getListSOUsecase,
                            ConfirmInputUsecase confirmInputUsecase,
                            GetListProductDetailGroupUsecase getListProductDetailGroupUsecase,
                            GetTimesInputAndOutputByDepartmentUsecase getTimesInputAndOutputByDepartmentUsecase, AddPhieuGiaoNhanUsecase addPhieuGiaoNhanUsecase, GetListMaPhieuGiaoUsecase getListMaPhieuGiaoUsecase, GetListInputUnConfirmByMaPhieuUsecase getListInputUnConfirmByMaPhieuUsecase) {
        this.view = view;
        this.getListSOUsecase = getListSOUsecase;
        this.confirmInputUsecase = confirmInputUsecase;
        this.getListProductDetailGroupUsecase = getListProductDetailGroupUsecase;
        this.getTimesInputAndOutputByDepartmentUsecase = getTimesInputAndOutputByDepartmentUsecase;
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
    public void getListTimes(long orderId) {
        getTimesInputAndOutputByDepartmentUsecase.executeIO(new GetTimesInputAndOutputByDepartmentUsecase.RequestValue(orderId, UserManager.getInstance().getUser().getRole()),
                new BaseUseCase.UseCaseCallback<GetTimesInputAndOutputByDepartmentUsecase.ResponseValue, GetTimesInputAndOutputByDepartmentUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetTimesInputAndOutputByDepartmentUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        view.showListTimes(successResponse.getEntity().getListTimesInput());
                    }

                    @Override
                    public void onError(GetTimesInputAndOutputByDepartmentUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
    }

    @Override
    public void getListConfirm(long maPhieuId,int times, boolean refresh) {
        view.showProgressBar();
        getListInputUnConfirmByMaPhieuUsecase.executeIO(new GetListInputUnConfirmByMaPhieuUsecase.RequestValue(maPhieuId),
                new BaseUseCase.UseCaseCallback<GetListInputUnConfirmByMaPhieuUsecase.ResponseValue,
                        GetListInputUnConfirmByMaPhieuUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListInputUnConfirmByMaPhieuUsecase.ResponseValue successResponse) {
                        ListOrderConfirmManager.getInstance().setListOrder(successResponse.getEntity());
                        if (!refresh) {
                            view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_list_confirm_success));
                        }
                        view.hideProgressBar();
                        if (times > 0){
                            getListConfirm(times);
                        }

                    }

                    @Override
                    public void onError(GetListInputUnConfirmByMaPhieuUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        if (!refresh) {
                            view.showError(errorResponse.getDescription());
                            ListOrderConfirmManager.getInstance().setListOrder(new ArrayList<>());

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
                        view.showListDeliveryCode(successResponse.getEntity());
                        ListDeliveryNoteManager.getInstance().setListDeliveryNote(successResponse.getEntity());
                        getListTimes(orderId);
                    }

                    @Override
                    public void onError(GetListMaPhieuGiaoUsecase.ErrorValue errorResponse) {

                        ListDeliveryNoteManager.getInstance().setListDeliveryNote(new ArrayList<>());
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());

                    }
                });
    }

    @Override
    public void getListDepartment() {
        view.showListDepartment(ListDepartmentManager.getInstance().getListDepartment(
                UserManager.getInstance().getUser().getRole()
        ));
    }

    @Override
    public void getListConfirm(int times) {
        localRepository.addOrderConfirm(ListOrderConfirmManager.getInstance().getListOrder(), times)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        localRepository.getListConfirm().subscribe(new Action1<RealmResults<LogScanConfirmModel>>() {
                            @Override
                            public void call(RealmResults<LogScanConfirmModel> logScanConfirmModels) {
                                view.showListConfirm(logScanConfirmModels);
                            }
                        });
                    }
                });


    }


    private boolean allowedToSave = false;

    @Override
    public void checkBarcode(String barcode, boolean groupCode) {
        barcode = barcode.toUpperCase();
        if (barcode.contains(CoreApplication.getInstance().getString(R.string.text_minus))) {
            showError(CoreApplication.getInstance().getString(R.string.text_barcode_error_type));
            return;
        }
        localRepository.findConfirmByBarcode(barcode).subscribe(new Action1<LogScanConfirmModel>() {
            @Override
            public void call(LogScanConfirmModel logScanConfirmModel) {
                if (logScanConfirmModel == null) {
                    showError(CoreApplication.getInstance().getString(R.string.text_barcode_no_exist));
                } else {

                    if (logScanConfirmModel.getDeliveryNoteModel().getNumberRest() == 0) {
                        showError(CoreApplication.getInstance().getString(R.string.text_number_confirm_residual));
                        return;
                    }
                    if (!groupCode) {
                        if (logScanConfirmModel.getNumberRestInTimes() == 0) {
                            showError(CoreApplication.getInstance().getString(R.string.text_number_confirm_residual));
                        } else {
                            saveConfirm(logScanConfirmModel.getOutputId(), 1);
                        }

                    } else {
                        int count = ListGroupManager.getInstance().countProductById(logScanConfirmModel.getProductDetailId());
                        if (count > 1) {
                            view.showDialogChooseGroup(ListGroupManager.getInstance().getListGroupEntityByProductId(logScanConfirmModel.getProductDetailId()));
                        } else if (count == 1) {
                            ProductGroupEntity productGroupEntity = ListGroupManager.getInstance().getProductById(logScanConfirmModel.getProductDetailId());
                            GroupEntity groupEntityList = ListGroupManager.getInstance().getGroupEntityByGroupCode(productGroupEntity.getGroupCode());
                            if (productGroupEntity == null) {
                                showError(CoreApplication.getInstance().getString(R.string.text_product_not_in_group));
                                return;
                            }
                            //saveNumberConfirmGroup(groupEntityList, maPhieuId, orderId, times, departmentId);
                        }


                    }
                }

            }
        });


    }

    @Override
    public void updateNumberConfirm(long outputId, int numberScan) {
        localRepository.updateNumnberLogConfirm(outputId, numberScan, false).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.setValuePrint(false);
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_barcode_success));
                view.startMusicSuccess();
                view.turnOnVibrator();
                view.hideProgressBar();
            }
        });
    }

    @Override
    public void uploadData(long maPhieuId, long orderId, int departmentIdOut, int times) {
        //    view.showError(CoreApplication.getInstance().getString(R.string.text_not_print_before_upload));
        view.showProgressBar();
        localRepository.getListLogScanConfirm().subscribe(new Action1<List<LogScanConfirmModel>>() {
            @Override
            public void call(List<LogScanConfirmModel> logScanConfirmModels) {

                List<LogScanConfirmModel> listUpload = new ArrayList<>();
                for (LogScanConfirmModel logScanConfirmModel : logScanConfirmModels) {
                    if (logScanConfirmModel.getStatusConfirm() != -1) {
                        listUpload.add(logScanConfirmModel);
                    }
                }
                GsonBuilder builder = new GsonBuilder();
                builder.excludeFieldsWithoutExposeAnnotation();
                Gson gson = builder.create();
                String json = gson.toJson(listUpload);

                List<PrintConfirmEntity> list = new ArrayList<>();
                for (LogScanConfirmModel logScanConfirmModel : logScanConfirmModels) {
                    PrintConfirmEntity detailItem = new PrintConfirmEntity(logScanConfirmModel.getDeliveryNoteModel().getProductId(),
                            logScanConfirmModel.getProductDetailId(), (int) logScanConfirmModel.getNumberOut(),
                            (int) logScanConfirmModel.getDeliveryNoteModel().getNumberUsed(), (int) logScanConfirmModel.getNumberConfirmed());

                    list.add(detailItem);
                }

                String maPhieu1 = ListDeliveryNoteManager.getInstance().getDeliveryNoteById(maPhieuId);
                DeliveryEntity deliveryEntity = new DeliveryEntity(maPhieu1, list);
                String jsonWithData = new Gson().toJson(deliveryEntity);
                confirmInputUsecase.executeIO(new ConfirmInputUsecase.RequestValue(UserManager.getInstance().getUser().getRole(),
                        json), new BaseUseCase.UseCaseCallback<ConfirmInputUsecase.ResponseValue,
                        ConfirmInputUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(ConfirmInputUsecase.ResponseValue successResponse) {
                        localRepository.updateStatusLogConfirm().subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                addPhieuGiaoNhanUsecase.executeIO(new AddPhieuGiaoNhanUsecase.RequestValue(orderId, UserManager.getInstance().getUser().getRole(),
                                        departmentIdOut, times, jsonWithData, UserManager.getInstance().getUser().getId()
                                ), new BaseUseCase.UseCaseCallback<AddPhieuGiaoNhanUsecase.ResponseValue,
                                        AddPhieuGiaoNhanUsecase.ErrorValue>() {
                                    @Override
                                    public void onSuccess(AddPhieuGiaoNhanUsecase.ResponseValue successResponse) {
                                        view.hideProgressBar();
                                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_upload_success));
                                        getListConfirm(maPhieuId,times, true);
                                    }

                                    @Override
                                    public void onError(AddPhieuGiaoNhanUsecase.ErrorValue errorResponse) {
                                        view.showError(errorResponse.getDescription());
                                        view.hideProgressBar();
                                    }
                                });
                                Log.d(TAG, "Print: " + jsonWithData);

                            }
                        });

                    }

                    @Override
                    public void onError(ConfirmInputUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
            }
        });
    }


    @Override
    public void getListGroupCode(long orderId) {
        view.showProgressBar();
        getListProductDetailGroupUsecase.executeIO(new GetListProductDetailGroupUsecase.RequestValue(orderId),
                new BaseUseCase.UseCaseCallback<GetListProductDetailGroupUsecase.ResponseValue,
                        GetListProductDetailGroupUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetListProductDetailGroupUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        ListGroupManager.getInstance().setListGroup(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetListProductDetailGroupUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        ListGroupManager.getInstance().setListGroup(new ArrayList<>());
                    }
                });
    }

    @Override
    public void saveIPAddress(String ipAddress, int port, long maPhieuId,  long orderId, int departmentIdOut, int times, long serverId, boolean upload) {
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
        localRepository.confirmAllProductReceive().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

                view.setValuePrint(false);
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_confirm_all_success));
                view.startMusicSuccess();
                view.turnOnVibrator();
                view.hideProgressBar();
            }
        });
    }

    @Override
    public void cancelConfirmAll() {
        localRepository.cancelConfirmAllProductReceive().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

                view.setValuePrint(false);
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_cancle_confirm_all_success));
                view.startMusicSuccess();
                view.turnOnVibrator();
                view.hideProgressBar();
            }
        });
    }


    private int numberLoop = 0;

    @Override
    public void saveNumberConfirmGroup(GroupEntity groupEntity, long maPhieuId, long orderId, int times, int departmentId) {
        allowedToSave = true;
        numberLoop = 0;
        for (ProductGroupEntity item : groupEntity.getProducGroupList()) {
            numberLoop++;
            OrderConfirmEntity orderConfirmEntity = ListOrderConfirmManager.getInstance().getDetailByProductDetailId(item.getProductDetailID());
            if (orderConfirmEntity != null) {
                localRepository.findConfirmByBarcode(orderConfirmEntity.getBarcode()).subscribe(new Action1<LogScanConfirmModel>() {
                    @Override
                    public void call(LogScanConfirmModel logScanConfirmModel) {
                        if (logScanConfirmModel.getNumberRestInTimes() >= item.getNumber()) {
                            if (logScanConfirmModel.getNumberRestInTimes() == 0) {
                                allowedToSave = false;
                            }
                        } else {
                            allowedToSave = false;
                        }
                        if (allowedToSave) {
                            if (numberLoop == groupEntity.getProducGroupList().size()) {
                                for (ProductGroupEntity item : groupEntity.getProducGroupList()) {
                                    OrderConfirmEntity orderConfirmEntity = ListOrderConfirmManager.getInstance().getDetailByProductDetailId(item.getProductDetailID());
                                    if (orderConfirmEntity != null) {
                                        localRepository.findConfirmByBarcode(orderConfirmEntity.getBarcode()).subscribe(new Action1<LogScanConfirmModel>() {
                                            @Override
                                            public void call(LogScanConfirmModel logScanConfirmModel) {
                                                saveConfirm(orderConfirmEntity.getOutputID(), item.getNumber());
                                            }
                                        });
                                    }
                                }
                            }
                        } else {
                            showError(CoreApplication.getInstance().getString(R.string.text_number_in_group_exceed_number_received_save_enough));
                        }

                    }
                });
            }

            if (!allowedToSave) {
                break;
            }
        }
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
                                localRepository.getListLogScanConfirm().subscribe(new Action1<List<LogScanConfirmModel>>() {
                                    @Override
                                    public void call(List<LogScanConfirmModel> logScanConfirms) {

                                        if (logScanConfirms.size() == 0) {
                                            view.hideProgressBar();
                                            view.showError(CoreApplication.getInstance().getString(R.string.text_not_data_print));
                                        } else {
                                            List<PrintConfirmEntity> list = new ArrayList<>();
                                            for (LogScanConfirmModel logScanConfirm : logScanConfirms) {
                                                PrintConfirmEntity detailItem = new PrintConfirmEntity(logScanConfirm.getDeliveryNoteModel().getProductId(),
                                                        logScanConfirm.getProductDetailId(), (int) logScanConfirm.getNumberOut(),
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

                                view.setValuePrint(true);
                                view.hideProgressBar();
                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_print_success));
                                if (upload) {
                                    uploadData(maPhieuId, orderId, departmentIdOut, times);
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

    public void saveConfirm(long outputId, int number) {
        localRepository.updateNumnberLogConfirm(outputId, number, true).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

                view.setValuePrint(false);
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
