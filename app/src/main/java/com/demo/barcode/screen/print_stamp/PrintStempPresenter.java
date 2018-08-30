package com.demo.barcode.screen.print_stamp;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.SocketRespone;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.LogListOrderPackaging;
import com.demo.architect.data.model.offline.LogScanPackaging;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.repository.base.socket.ConnectSocket;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.PostListCodeProductDetailUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListApartmentManager;
import com.demo.barcode.manager.ListModuleManager;
import com.demo.barcode.manager.UserManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class PrintStempPresenter implements PrintStempContract.Presenter {

    private final String TAG = PrintStempPresenter.class.getName();
    private final PrintStempContract.View view;
    private final PostListCodeProductDetailUsecase postListCodeProductDetailUsecase;
    private String jsonUpload;
    @Inject
    LocalRepository localRepository;

    @Inject
    PrintStempPresenter(@NonNull PrintStempContract.View view, PostListCodeProductDetailUsecase postListCodeProductDetailUsecase) {
        this.view = view;
        this.postListCodeProductDetailUsecase = postListCodeProductDetailUsecase;
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
    public void getOrderPackaging(int orderId) {
        localRepository.findOrderPackaging(orderId).subscribe(new Action1<LogListOrderPackaging>() {
            @Override
            public void call(LogListOrderPackaging logListOrderPackaging) {
                view.showOrderPackaging(logListOrderPackaging);
            }
        });
    }

    @Override
    public void getTotalScanBySerialPack(int orderId, int apartmentId, int moduleId, String serialPack) {
        localRepository.getTotalScanBySerialPack(orderId,apartmentId,moduleId,serialPack).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                view.showTotalNumberScan(integer);
            }
        });
    }

    @Override
    public void getListScanStages(int orderId, int apartmentId, int moduleId, String serialPack) {
        localRepository.getListScanPackaging(orderId, apartmentId, moduleId, serialPack)
                .subscribe(new Action1<List<LogScanPackaging>>() {
                    @Override
                    public void call(List<LogScanPackaging> logScanPackagings) {
                        view.showListScanPackaging(logScanPackagings);
                        GsonBuilder builder = new GsonBuilder();
                        builder.excludeFieldsWithoutExposeAnnotation();
                        Gson gson = builder.create();
                        jsonUpload = gson.toJson(logScanPackagings);
                    }
                });
    }

    @Override
    public void printTemp(int serverId) {
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
                        serverId, new ConnectSocket.onPostExecuteResult() {
                    @Override
                    public void onPostExecute(SocketRespone respone) {
                        if (respone.getConnect() == 1 && respone.getResult() == 1) {
                            if (serverId == 0) {
                                postListCodeProductDetailUsecase.executeIO(new PostListCodeProductDetailUsecase.RequestValue(jsonUpload, user.getId(),
                                        ""), new BaseUseCase.UseCaseCallback<PostListCodeProductDetailUsecase.ResponseValue,
                                        PostListCodeProductDetailUsecase.ErrorValue>() {
                                    @Override
                                    public void onSuccess(PostListCodeProductDetailUsecase.ResponseValue successResponse) {

                                        printTemp(successResponse.getId());
                                    }

                                    @Override
                                    public void onError(PostListCodeProductDetailUsecase.ErrorValue errorResponse) {
                                        view.showError(errorResponse.getDescription());
                                        view.hideProgressBar();
                                    }
                                });

                            } else {
                                localRepository.updateStatusScanPackaging(serverId).subscribe(new Action1<String>() {
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
    public void getApartment(int apartmentId) {
        ApartmentEntity apartment= ListApartmentManager.getInstance().getApartmentById(apartmentId);
        view.showApartmentName(apartment.getApartmentName());
    }

    @Override
    public void getModule(int moduleId) {
        ModuleEntity module= ListModuleManager.getInstance().getModuleById(moduleId);
        view.showModuleName(module.getModuleName());
    }
}
