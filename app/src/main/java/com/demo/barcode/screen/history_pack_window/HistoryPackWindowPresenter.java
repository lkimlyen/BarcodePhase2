package com.demo.barcode.screen.history_pack_window;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demo.architect.data.model.SocketRespone;
import com.demo.architect.data.model.offline.IPAddress;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.repository.base.socket.ConnectSocket;
import com.demo.architect.domain.AddScanTemHangCuaUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetApartmentUsecase;
import com.demo.architect.domain.GetHistoryIntemCuaUsecase;
import com.demo.architect.domain.GetListSOUsecase;
import com.demo.architect.domain.GetProductSetUsecase;
import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.manager.ListHistoryManager;
import com.demo.barcode.manager.ListHistoryPackWindowManager;
import com.demo.barcode.manager.ListSOManager;
import com.demo.barcode.manager.ListSetWindowManager;
import com.demo.barcode.manager.UserManager;
import com.demo.barcode.util.ConvertUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class HistoryPackWindowPresenter implements HistoryPackWindowContract.Presenter {

    private final String TAG = HistoryPackWindowPresenter.class.getName();
    private final HistoryPackWindowContract.View view;
    private final GetListSOUsecase getListSOUsecase;
    private final GetHistoryIntemCuaUsecase getHistoryIntemCuaUsecase;
    private final GetProductSetUsecase getProductSetUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    HistoryPackWindowPresenter(@NonNull HistoryPackWindowContract.View view, GetListSOUsecase getListSOUsecase,
                               GetHistoryIntemCuaUsecase getHistoryIntemCuaUsecase, GetProductSetUsecase getProductSetUsecase) {
        this.view = view;

        this.getListSOUsecase = getListSOUsecase;
        this.getHistoryIntemCuaUsecase = getHistoryIntemCuaUsecase;
        this.getProductSetUsecase = getProductSetUsecase;
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
                        view.showError(errorResponse.getDescription());
                        ListSOManager.getInstance().setListSO(new ArrayList<>());
                        //   view.clearDataNoProduct(true);
                    }
                });
    }



    @Override
    public void getListHistory(long productSetId, int direction) {
        view.showProgressBar();
        getHistoryIntemCuaUsecase.executeIO(new GetHistoryIntemCuaUsecase.RequestValue(productSetId, direction),
                new BaseUseCase.UseCaseCallback<GetHistoryIntemCuaUsecase.ResponseValue,
                        GetHistoryIntemCuaUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetHistoryIntemCuaUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        if(successResponse.getEntity().size() == 0){
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_data));
                        }
                            view.showListHistory(successResponse.getEntity());

                        ListHistoryPackWindowManager.getInstance().setListHitory(successResponse.getEntity());
                    }

                    @Override
                    public void onError(GetHistoryIntemCuaUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListHistoryPackWindowManager.getInstance().setListHitory(new ArrayList<>());
                    }
                });
    }

    @Override
    public void print(long id) {
        localRepository.findIPAddress().subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                if (address == null) {
                    //view.showError(CoreApplication.getInstance().getString(R.string.text_no_ip_address));
                    view.showDialogCreateIPAddress(id);
                    return;
                }
                view.showProgressBar();
                ConnectSocket connectSocket = new ConnectSocket(address.getIpAddress(), address.getPortNumber(),
                        id, 5, new ConnectSocket.onPostExecuteResult() {
                    @Override
                    public void onPostExecute(SocketRespone respone) {
                        if (respone.getConnect() == 1 && respone.getResult() == 1) {

                                        view.hideProgressBar();
                                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_print_success));

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
    public void saveIPAddress(String ipAddress, int port, long id) {
        long userId = UserManager.getInstance().getUser().getId();
        IPAddress model = new IPAddress(1, ipAddress, port, userId, ConvertUtils.getDateTimeCurrent());
        localRepository.insertOrUpdateIpAddress(model).subscribe(new Action1<IPAddress>() {
            @Override
            public void call(IPAddress address) {
                //  view.showIPAddress(address);
                print(id);
            }
        });
    }


    @Override
    public void getListSetWindow(long orderId) {
        view.showProgressBar();
        getProductSetUsecase.executeIO(new GetProductSetUsecase.RequestValue(orderId),
                new BaseUseCase.UseCaseCallback<GetProductSetUsecase.ResponseValue,
                        GetProductSetUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetProductSetUsecase.ResponseValue successResponse) {
                        ListSetWindowManager.getInstance().setListSet(successResponse.getEntity());
                        view.showListSetWindow(successResponse.getEntity());
                        view.hideProgressBar();
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_get_set_window_success));

                    }

                    @Override
                    public void onError(GetProductSetUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                        ListSetWindowManager.getInstance().setListSet(new ArrayList<>());
                    }
                });
    }


}
