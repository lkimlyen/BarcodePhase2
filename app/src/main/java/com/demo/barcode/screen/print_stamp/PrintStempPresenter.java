package com.demo.barcode.screen.print_stamp;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.ListModuleEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.SocketRespone;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.model.offline.LogListSerialPackPagkaging;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.repository.base.socket.ConnectSocket;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetCodePackUsecase;
import com.demo.architect.domain.PostListCodeProductDetailUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListApartmentManager;
import com.demo.barcode.manager.ListModulePackagingManager;
import com.demo.barcode.manager.ListSOManager;
import com.demo.barcode.manager.UserManager;
import com.demo.barcode.util.ConvertUtils;

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
    private final GetCodePackUsecase getCodePackUsecase;
    private String jsonUpload;
    @Inject
    LocalRepository localRepository;

    @Inject
    PrintStempPresenter(@NonNull PrintStempContract.View view, PostListCodeProductDetailUsecase postListCodeProductDetailUsecase, GetCodePackUsecase getCodePackUsecase) {
        this.view = view;
        this.postListCodeProductDetailUsecase = postListCodeProductDetailUsecase;
        this.getCodePackUsecase = getCodePackUsecase;
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
    public void getTotalScanBySerialPack(long orderId, long apartmentId, long productId, String serialPack) {
        localRepository.getTotalScanBySerialPack( productId, serialPack).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                view.showTotalNumberScan(integer);
            }
        });
    }

    @Override
    public void getListDetailPackageById(long logSerialId) {
        localRepository.getListDetailPackageById(logSerialId)
                .subscribe(new Action1<LogListSerialPackPagkaging>() {
                    @Override
                    public void call(LogListSerialPackPagkaging logScanPackagings) {
                        localRepository.getTotalScanBySerialPack( logScanPackagings.getProductId(), logScanPackagings.getSerialPack()).subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                view.showListScanPackaging(logScanPackagings, logScanPackagings.getNumberTotal() == integer);

                            }
                        });
                    }
                });
    }

    @Override
    public void printTemp(long orderId, long apartmentId, long moduleId, String serialPack, long serverId,long logSerialId) {
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
                        serverId, 1, new ConnectSocket.onPostExecuteResult() {
                    @Override
                    public void onPostExecute(SocketRespone respone) {
                        if (respone.getConnect() == 1 && respone.getResult() == 1) {
                            if (serverId == 0) {
                                localRepository.getListDetailUploadPackageById(logSerialId).subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String json) {
                                        postListCodeProductDetailUsecase.executeIO(new PostListCodeProductDetailUsecase.RequestValue(json, user.getId(),
                                                null), new BaseUseCase.UseCaseCallback<PostListCodeProductDetailUsecase.ResponseValue,
                                                PostListCodeProductDetailUsecase.ErrorValue>() {
                                            @Override
                                            public void onSuccess(PostListCodeProductDetailUsecase.ResponseValue successResponse) {

                                                printTemp(orderId, apartmentId, moduleId, serialPack, successResponse.getId(),logSerialId);
                                            }

                                            @Override
                                            public void onError(PostListCodeProductDetailUsecase.ErrorValue errorResponse) {
                                                view.showError(errorResponse.getDescription());
                                                view.hideProgressBar();
                                            }
                                        });
                                    }
                                });



                            } else {
                                localRepository.updateStatusScanPackaging(logSerialId, serverId).subscribe(new Action1<String>() {
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
    public void getApartment(long apartmentId) {
        ApartmentEntity apartment = ListApartmentManager.getInstance().getApartmentById(apartmentId);
        view.showApartmentName(apartment.getApartmentName());
    }

    @Override
    public void getModule(long moduleId, String serialPack) {
        ListModuleEntity module = ListModulePackagingManager.getInstance().getModuleById(moduleId);
        view.showModuleName(module.getModule());
        List<PackageEntity> list = module.getPackageList();
        for (PackageEntity packageEntity : list) {
            if (packageEntity.getSerialPack().equals(serialPack)) {
                view.showSerialPack(packageEntity);
                break;
            }
        }
    }

    @Override
    public void saveIPAddress(String ipAddress, int port, long orderId, long apartmentId, long moduleId, String serialPack, long serverId,long logSerialId) {
        long userId = UserManager.getInstance().getUser().getId();
        IPAddress model = new IPAddress(1, ipAddress, port, userId, ConvertUtils.getDateTimeCurrent());
        localRepository.insertOrUpdateIpAddress(model).subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                //  view.showIPAddress(address);
                printTemp(orderId, apartmentId, moduleId, serialPack, serverId,logSerialId);
            }
        });
    }

    @Override
    public void getListCodePack(long orderId, int orderType, long productId) {
        view.showProgressBar();
        getCodePackUsecase.executeIO(new GetCodePackUsecase.RequestValue(orderId, orderType, productId),
                new BaseUseCase.UseCaseCallback<GetCodePackUsecase.ResponseValue,
                        GetCodePackUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetCodePackUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        view.showListCodePack(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetCodePackUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
    }

    private int numberProduct = 0;

    @Override
    public boolean checkNumberProduct( long productId,String sttPack) {
        numberProduct = 0;
        localRepository.getTotalScanBySerialPack( productId, sttPack).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                numberProduct = integer;
            }
        });
        return ListModulePackagingManager.getInstance().getPackingBySerialPack(productId, sttPack).getTotal() == numberProduct;
    }


}
