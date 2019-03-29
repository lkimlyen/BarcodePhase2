package com.demo.barcode.screen.print_stamp_window;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.SetWindowEntity;
import com.demo.architect.data.model.SocketRespone;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.ListPackCodeWindowModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.repository.base.socket.ConnectSocket;
import com.demo.architect.domain.AddScanTemHangCuaUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListSOManager;
import com.demo.barcode.manager.ListSetWindowManager;
import com.demo.barcode.manager.UserManager;
import com.demo.barcode.util.ConvertUtils;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class PrintStempWindowPresenter implements PrintStempWindowContract.Presenter {

    private final String TAG = PrintStempWindowPresenter.class.getName();
    private final PrintStempWindowContract.View view;
    private final AddScanTemHangCuaUsecase addScanTemHangCuaUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    PrintStempWindowPresenter(@NonNull PrintStempWindowContract.View view,
                              AddScanTemHangCuaUsecase addScanTemHangCuaUsecase) {
        this.view = view;
        this.addScanTemHangCuaUsecase = addScanTemHangCuaUsecase;
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
    public void getOrderPackaging(long orderId) {
        SOEntity soEntity = ListSOManager.getInstance().getSOById(orderId);
        view.showOrderPackaging(soEntity);
    }


    @Override
    public void getListDetailPackageById(long mainId) {
        localRepository.getListDetailPackWindowById(mainId)
                .subscribe(new Action1<ListPackCodeWindowModel>() {
                    @Override
                    public void call(ListPackCodeWindowModel logScanPackagings) {
                        localRepository.getTotalNumberDetaiLInPackWindow(logScanPackagings.getPackCode(), logScanPackagings.getNumberSetOnPack()).subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                view.showListScanPackaging(logScanPackagings);
                                view.showTotalNumberScan(integer);
                            }
                        });
                    }
                });
    }

    @Override
    public void printTemp(long orderId, long productSetId, int direction,String packCode,int numberOnPack, long serverId, long mainId) {
        UserEntity user = UserManager.getInstance().getUser();
        localRepository.findIPAddress().subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                if (address == null) {
                    //view.showError(CoreApplication.getInstance().getString(R.string.text_no_ip_address));
                    view.showDialogCreateIPAddress();
                    return;
                }
                view.showProgressBar();
                ConnectSocket connectSocket = new ConnectSocket(address.getIpAddress(), address.getPortNumber(),
                        serverId, 5, new ConnectSocket.onPostExecuteResult() {
                    @Override
                    public void onPostExecute(SocketRespone respone) {
                        if (respone.getConnect() == 1 && respone.getResult() == 1) {
                            if (serverId == 0) {
                                localRepository.getListDetailUploadPackWindowById(mainId).subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String json) {
                                        addScanTemHangCuaUsecase.executeIO(new AddScanTemHangCuaUsecase.RequestValue(orderId,
                                                productSetId,direction,packCode,numberOnPack,user.getId(),json), new BaseUseCase.UseCaseCallback<AddScanTemHangCuaUsecase.ResponseValue,
                                                AddScanTemHangCuaUsecase.ErrorValue>() {
                                            @Override
                                            public void onSuccess(AddScanTemHangCuaUsecase.ResponseValue successResponse) {

                                                printTemp(orderId, productSetId,direction,packCode,numberOnPack, successResponse.getId(), mainId);
                                            }

                                            @Override
                                            public void onError(AddScanTemHangCuaUsecase.ErrorValue errorResponse) {
                                                view.showError(errorResponse.getDescription());
                                                view.hideProgressBar();
                                            }
                                        });
                                    }
                                });


                            } else {
                                localRepository.updateStatusScanPackagingWindow(mainId, serverId).subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.hideProgressBar();
                                        view.startActivityCreate();
                                    }
                                });
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
    public void getSetWindow(long productSetId) {
        SetWindowEntity apartment = ListSetWindowManager.getInstance().getSetById(productSetId);
        view.showProductSetName(apartment.getProductSetName());
    }


    @Override
    public void saveIPAddress(String ipAddress, int port, long orderId, long productSetId, int direction,String packCode,int numberOnPack, long serverId, long logSerialId) {
        long userId = UserManager.getInstance().getUser().getId();
        IPAddress model = new IPAddress(1, ipAddress, port, userId, ConvertUtils.getDateTimeCurrent());
        localRepository.insertOrUpdateIpAddress(model).subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                //  view.showIPAddress(address);
                printTemp(orderId, productSetId,direction,packCode,numberOnPack, serverId, logSerialId);
            }
        });
    }

}
