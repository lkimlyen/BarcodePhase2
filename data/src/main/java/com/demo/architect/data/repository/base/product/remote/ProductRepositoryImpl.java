package com.demo.architect.data.repository.base.product.remote;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;

import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Skull on 04/01/2018.
 */

public class ProductRepositoryImpl implements ProductRepository {
    private final static String TAG = ProductRepositoryImpl.class.getName();

    private ProductApiInterface mRemoteApiInterface;
    private Context context;
    private String server;

    public ProductRepositoryImpl(ProductApiInterface mRemoteApiInterface, Context context) {
        this.mRemoteApiInterface = mRemoteApiInterface;
        this.context = context;
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
    }

    private void handleProductResponse(Call<BaseListResponse<ProductEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<ProductEntity> response = call.execute().body();
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

    private void handleProductGroupResponse(Call<BaseListResponse<ProductGroupEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<ProductGroupEntity> response = call.execute().body();
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

    private void handleStringResponse(Call<BaseResponse<String>> call, Subscriber subscriber) {
        try {
            BaseResponse<String> response = call.execute().body();
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
    public Observable<BaseListResponse<ProductEntity>> getInputForProductDetail(final int orderId, final int departmentId) {

        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<ProductEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<ProductEntity>> subscriber) {
                handleProductResponse(mRemoteApiInterface.getInputForProductDetail(
                        server + "/WS/api/GD2GetInputForProductDetail", orderId, departmentId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse<String>> groupProductDetail(final String key, final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseResponse<String>>() {
            @Override
            public void call(Subscriber<? super BaseResponse<String>> subscriber) {
                handleStringResponse(mRemoteApiInterface.groupProductDetail(
                        server + "/WS/api/GD2GroupProductDetail", key, json), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<ProductGroupEntity>> getListProductDetailGroup(final int orderId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<ProductGroupEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<ProductGroupEntity>> subscriber) {
                handleProductGroupResponse(mRemoteApiInterface.getListProductDetailGroup(
                        server + "/WS/api/GD2GetListProductDetailGroup", orderId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> deactiveProductDetailGroup(final String key, final String groupCode, final int userId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.deactiveProductDetailGroup(
                        server + "/WS/api/GD2DeactiveProductDetailGroup", key, groupCode,userId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> updateProductDetailGroup(final String key, final String groupCode,
                                                             final String jsonNew, final String jsonUpdate,
                                                             final String jsonDelete, final int userId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.updateProductDetailGroup(
                        server + "/WS/api/GD2UpdateProductDetailGroup", key, groupCode,
                        jsonNew, jsonUpdate, jsonDelete, userId), subscriber);
            }
        });
    }
}
