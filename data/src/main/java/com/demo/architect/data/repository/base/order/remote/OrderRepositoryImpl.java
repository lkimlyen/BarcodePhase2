package com.demo.architect.data.repository.base.order.remote;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.SOEntity;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Skull on 04/01/2018.
 */

public class OrderRepositoryImpl implements OrderRepository {
    private final static String TAG = OrderRepositoryImpl.class.getName();

    private OrderApiInterface mRemoteApiInterface;
    private Context context;
    private String server;

    public OrderRepositoryImpl(OrderApiInterface mRemoteApiInterface, Context context) {
        this.mRemoteApiInterface = mRemoteApiInterface;
        this.context = context;
    }

    private void handleSOResponse(Call<BaseListResponse<SOEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<SOEntity> response = call.execute().body();
            if (!subscriber.isUnsubscribed()) {
                if (response != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(new Exception("Network Error!"));
                }
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }
    }


    private void handleOrderConfirmResponse(Call<BaseListResponse<OrderConfirmEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<OrderConfirmEntity> response = call.execute().body();
            if (!subscriber.isUnsubscribed()) {
                if (response != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(new Exception("Network Error!"));
                }
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }
    }

    private void handleBaseResponse(Call<BaseListResponse> call, Subscriber subscriber) {
        try {
            BaseListResponse response = call.execute().body();
            if (!subscriber.isUnsubscribed()) {
                if (response != null) {
                    subscriber.onNext(response);
                } else {
                    subscriber.onError(new Exception("Network Error!"));
                }
                subscriber.onCompleted();
            }
        } catch (Exception e) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onError(e);
                subscriber.onCompleted();
            }
        }
    }


    @Override
    public Observable<BaseListResponse<SOEntity>> getListSO(final int orderType) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<SOEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<SOEntity>> subscriber) {
                handleSOResponse(mRemoteApiInterface.getListSO(
                        server + "/WS/api/GD2GetListSO", orderType), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<OrderConfirmEntity>> getInputUnConfirmed(final int orderId, final int departmentIDIn, final int departmentIDOut) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<OrderConfirmEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<OrderConfirmEntity>> subscriber) {
                handleOrderConfirmResponse(mRemoteApiInterface.getInputUnConfirmed(
                        server + "/WS/api/GD2GetInputUnConfirmed", orderId, departmentIDIn, departmentIDOut), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse> scanProductDetailOut(final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse>() {
            @Override
            public void call(Subscriber<? super BaseListResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.scanProductDetailOut(
                        server + "/WS/api/GD2ScanProductDetailOut", json), subscriber);
            }
        });
    }
}
