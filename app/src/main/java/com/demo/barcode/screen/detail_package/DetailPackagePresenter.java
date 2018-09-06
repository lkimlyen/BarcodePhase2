package com.demo.barcode.screen.detail_package;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.SocketRespone;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.repository.base.socket.ConnectSocket;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListApartmentManager;
import com.demo.barcode.manager.ListCodePackManager;
import com.demo.barcode.manager.ListHistoryManager;
import com.demo.barcode.manager.ListModuleManager;
import com.demo.barcode.manager.ListSOManager;
import com.demo.barcode.manager.UserManager;
import com.demo.barcode.util.ConvertUtils;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class DetailPackagePresenter implements DetailPackageContract.Presenter {

    private final String TAG = DetailPackagePresenter.class.getName();
    private final DetailPackageContract.View view;
    @Inject
    LocalRepository localRepository;

    @Inject
    DetailPackagePresenter(@NonNull DetailPackageContract.View view) {
        this.view = view;
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
       SOEntity soEntity = ListSOManager.getInstance().getSOById(orderId);
       view.showOrder(soEntity);
    }

    @Override
    public void getTotalScan(int packageId) {
        int total = ListHistoryManager.getInstance().getHistoryById(packageId).totalQuantity();
        view.showTotalNumberScan(total);
    }

    @Override
    public void getListProductHistory(int packageId) {
        List<ProductPackagingEntity> list = ListHistoryManager.getInstance().getHistoryById(packageId).getList();
        view.showListProductHistory(list);
    }

    @Override
    public void printTemp(int serverId, String note, int packageId) {
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
                                printTemp(packageId, note, packageId);

                            } else {
                                view.hideProgressBar();
                                view.startActivityHistory();
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
        ApartmentEntity apartment = ListApartmentManager.getInstance().getApartmentById(apartmentId);
        view.showApartmentName(apartment.getApartmentName());
    }

    @Override
    public void getModule(int moduleId) {
        ModuleEntity module = ListModuleManager.getInstance().getModuleById(moduleId);
        view.showModuleName(module.getModuleName());
    }

    @Override
    public void getCodePack(String serialPack) {
        CodePackEntity codePack = ListCodePackManager.getInstance().getCodePackById(serialPack);
        view.showCodePack(codePack.getPackCode());
    }

    @Override
    public void saveIPAddress(String ipAddress, int port, int serverId, String note, int packageId) {
        int userId = UserManager.getInstance().getUser().getId();
        IPAddress model = new IPAddress(1, ipAddress, port, userId, ConvertUtils.getDateTimeCurrent());
        localRepository.insertOrUpdateIpAddress(model).subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                //  view.showIPAddress(address);
                printTemp(serverId,note,packageId);
            }
        });
    }
}
