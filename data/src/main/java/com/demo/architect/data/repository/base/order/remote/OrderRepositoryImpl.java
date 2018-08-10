package com.demo.architect.data.repository.base.order.remote;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.ListCodeOutEntityResponse;
import com.demo.architect.data.model.OrderACRResponse;
import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ResultEntity;
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

    private void handleSOResponse(Call<BaseResponse<SOEntity>> call, Subscriber subscriber) {
        try {
            BaseResponse<SOEntity> response = call.execute().body();
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
    private void handleBaseResponse(Call<BaseResponse> call, Subscriber subscriber) {
        try {
            BaseResponse response = call.execute().body();
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
    public Observable<BaseResponse<SOEntity>> getListSO(final int orderType) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseResponse<SOEntity>>() {
            @Override
            public void call(Subscriber<? super BaseResponse<SOEntity>> subscriber) {
                handleSOResponse(mRemoteApiInterface.getListSO(
                        server + "/WS/api/GD2GetListSO", orderType), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> scanProductDetailOut(final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.scanProductDetailOut(
                        server + "/WS/api/GD2ScanProductDetailOut", json), subscriber);
            }
        });
    }
}
