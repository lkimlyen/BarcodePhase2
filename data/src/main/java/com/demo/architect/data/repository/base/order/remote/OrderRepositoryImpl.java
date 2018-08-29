package com.demo.architect.data.repository.base.order.remote;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
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

    private void handleCodePackResponse(Call<BaseListResponse<CodePackEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<CodePackEntity> response = call.execute().body();
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

    private void handleModuleResponse(Call<BaseListResponse<ModuleEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<ModuleEntity> response = call.execute().body();
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
    private void handleProductPackagingResponse(Call<BaseListResponse<ProductPackagingEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<ProductPackagingEntity> response = call.execute().body();
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
    public Observable<BaseListResponse> scanProductDetailOut(final String key, final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse>() {
            @Override
            public void call(Subscriber<? super BaseListResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.scanProductDetailOut(
                        server + "/WS/api/GD2ScanProductDetailOut", key, json), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse> confirmInput(final String key, final int departmentId, final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse>() {
            @Override
            public void call(Subscriber<? super BaseListResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.confirmInput(
                        server + "/WS/api/GD2ComfirmInput", key, departmentId, json), subscriber);
            }
        });

    }

    @Override
    public Observable<BaseListResponse<CodePackEntity>> getCodePack(final int orderId, final int orderType, final int productId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<CodePackEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<CodePackEntity>> subscriber) {
                handleCodePackResponse(mRemoteApiInterface.getCodePack(
                        server + "/WS/api/GD2GetCodePack", orderId, orderType, productId), subscriber);
            }
        });

    }

    @Override
    public Observable<BaseListResponse<ModuleEntity>> getModule(final int orderId, final int orderType, final int departmentId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<ModuleEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<ModuleEntity>> subscriber) {
                handleModuleResponse(mRemoteApiInterface.getModule(
                        server + "/WS/api/GD2GetModule", orderId, orderType, departmentId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<ProductPackagingEntity>> postCheckBarCode(final int orderId, final int productId, final int apartmentId, final String packCode, final String sttPack, final String code) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<ProductPackagingEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<ProductPackagingEntity>> subscriber) {
                handleProductPackagingResponse(mRemoteApiInterface.postCheckBarCode(
                        server + "/WS/api/GD2PostCheckBarCode", orderId, productId, apartmentId,
                        packCode, sttPack, code), subscriber);
            }
        });
    }
}
