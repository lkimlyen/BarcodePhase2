package com.demo.architect.data.repository.base.product.remote;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.HistoryPackWindowEntity;
import com.demo.architect.data.model.ListModuleEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.ProductPackagingWindowEntity;
import com.demo.architect.data.model.ProductWarehouseEntity;
import com.demo.architect.data.model.ProductWindowEntity;
import com.demo.architect.data.model.SetWindowEntity;

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

    private void handleProductWindowResponse(Call<BaseListResponse<ProductWindowEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<ProductWindowEntity> response = call.execute().body();
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

    private void handleProductWarehouseResponse(Call<BaseListResponse<ProductWarehouseEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<ProductWarehouseEntity> response = call.execute().body();
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
    private void handleProductPackingWindowResponse(Call<BaseListResponse<ProductPackagingWindowEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<ProductPackagingWindowEntity> response = call.execute().body();
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

    private void handleHistoryPackWindowResponse(Call<BaseListResponse<HistoryPackWindowEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<HistoryPackWindowEntity> response = call.execute().body();
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
    private void handleProductGroupResponse(Call<BaseListResponse<GroupEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<GroupEntity> response = call.execute().body();
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
    private void handleIntegerResponse(Call<BaseResponse<Integer>> call, Subscriber subscriber) {
        try {
            BaseResponse<Integer> response = call.execute().body();
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

    private void handleModulePackagingResponse(Call<BaseListResponse<ListModuleEntity>> call, Subscriber subscriber) {
        try {
            BaseListResponse<ListModuleEntity> response = call.execute().body();
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
    public Observable<BaseListResponse<ProductEntity>> getInputForProductDetail(final long orderId, final int departmentId) {

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
    public Observable<BaseListResponse<ProductWindowEntity>> getInputForProductDetailWindow(final long orderId, final int departmentId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<ProductWindowEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<ProductWindowEntity>> subscriber) {
                handleProductWindowResponse(mRemoteApiInterface.getInputForProductDetailWindow(
                        server + "/WS/api/GD2GetInputForDetailCua", orderId, departmentId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<ProductWarehouseEntity>> getInputForProductWarehouse(final String key,final long orderId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<ProductWarehouseEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<ProductWarehouseEntity>> subscriber) {
                handleProductWarehouseResponse(mRemoteApiInterface.getInputForProductWarehouse(
                        server + "/WS/api/GD1GetProductInStore",key, orderId), subscriber);
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
    public Observable<BaseListResponse<GroupEntity>> getListProductDetailGroup(final long orderId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<GroupEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<GroupEntity>> subscriber) {
                handleProductGroupResponse(mRemoteApiInterface.getListProductDetailGroup(
                        server + "/WS/api/GD2GetListProductDetailGroup", orderId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<GroupEntity>> checkUpdateForGroup(final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<GroupEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<GroupEntity>> subscriber) {
                handleProductGroupResponse(mRemoteApiInterface.checkUpdateForGroup(
                        server + "/WS/api/GD2CheckUpdateForGroup", json), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> deactiveProductDetailGroup(final String key, final long masterGroupId, final long userId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.deactiveProductDetailGroup(
                        server + "/WS/api/GD2DeactiveProductDetailGroup", key, masterGroupId, userId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse> updateProductDetailGroup(final String key, final long masterGroupId,
                                                             final String jsonNew, final String jsonUpdate,
                                                             final String jsonDelete, final long userId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                handleBaseResponse(mRemoteApiInterface.updateProductDetailGroup(
                        server + "/WS/api/GD2UpdateProductDetailGroup", key, masterGroupId,
                        jsonNew, jsonUpdate, jsonDelete, userId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse<Integer>> postListCodeProductDetail(final String key, final String json, final long userId, final String note) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseResponse<Integer>>() {
            @Override
            public void call(Subscriber<? super BaseResponse<Integer>> subscriber) {
                handleIntegerResponse(mRemoteApiInterface.postListCodeProductDetail(
                        server + "/WS/api/GD2PostListCodeProductDetail", key, json,
                        userId, note), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse<Integer>> addScanTemHangCua(final String key, final long orderId, final long productSetId, final int direction, final String packCode, final int numberOnPack, final long userId, final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseResponse<Integer>>() {
            @Override
            public void call(Subscriber<? super BaseResponse<Integer>> subscriber) {
                handleIntegerResponse(mRemoteApiInterface.addScanTemHangCua(
                        server + "/WS/api/GD2AddScanTemHangCua", key,orderId,productSetId,
                        direction,packCode,numberOnPack,userId,json
                        ), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<ListModuleEntity>> getListProductInPackage(final long orderId,final long apartmentId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<ListModuleEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<ListModuleEntity>> subscriber) {
                handleModulePackagingResponse(mRemoteApiInterface.getListProductInPackage(
                        server + "/WS/api/GD2GetListProductInPackage", orderId,  apartmentId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseResponse<Integer>> addPhieuGiaoNhan(final String key, final long orderId, final int departmentInID,
                                                              final int departmentOutID, final int number, final String data, final long userId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseResponse<Integer>>() {
            @Override
            public void call(Subscriber<? super BaseResponse<Integer>> subscriber) {
                handleIntegerResponse(mRemoteApiInterface.addPhieuGiaoNhan(
                        server + "/WS/api/GD2AddPhieuGiaoNhan", key, orderId,departmentInID, departmentOutID, number,data,
                        userId), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<ProductPackagingWindowEntity>> getProductSetDetailBySetAndDirec(final long productSetId, final int direction) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<ProductPackagingWindowEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<ProductPackagingWindowEntity>> subscriber) {
                handleProductPackingWindowResponse(mRemoteApiInterface.getProductSetDetailBySetAndDirec(
                        server + "/WS/api/GD2GetProductSetDetailBySetAndDirec",productSetId,direction), subscriber);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<HistoryPackWindowEntity>> getHistoryIntemCua(final long productSetId, final int direction) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new Observable.OnSubscribe<BaseListResponse<HistoryPackWindowEntity>>() {
            @Override
            public void call(Subscriber<? super BaseListResponse<HistoryPackWindowEntity>> subscriber) {
                handleHistoryPackWindowResponse(mRemoteApiInterface.getHistoryIntemCua(
                        server + "/WS/api/GD2GetHistoryIntemCua",productSetId,direction), subscriber);
            }
        });
    }
}
