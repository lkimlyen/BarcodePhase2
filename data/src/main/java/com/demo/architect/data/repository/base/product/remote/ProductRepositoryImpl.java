package com.demo.architect.data.repository.base.product.remote;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.DepartmentEntity;
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
import com.demo.architect.data.model.UploadEntity;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import io.reactivex.Observable;
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
    private void handleProductResponse(Call<BaseListResponse<ProductEntity>> call, ObservableEmitter<BaseListResponse<ProductEntity>> emitter) {
        try {
            BaseListResponse<ProductEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleProductWindowResponse(Call<BaseListResponse<ProductWindowEntity>> call, ObservableEmitter<BaseListResponse<ProductWindowEntity>> emitter) {
        try {
            BaseListResponse<ProductWindowEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleProductWarehouseResponse(Call<BaseListResponse<ProductWarehouseEntity>> call, ObservableEmitter<BaseListResponse<ProductWarehouseEntity>> emitter) {
        try {
            BaseListResponse<ProductWarehouseEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    } private void handleProductPackingWindowResponse(Call<BaseListResponse<ProductPackagingWindowEntity>> call, ObservableEmitter<BaseListResponse<ProductPackagingWindowEntity>> emitter) {
        try {
            BaseListResponse<ProductPackagingWindowEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleHistoryPackWindowResponse(Call<BaseListResponse<HistoryPackWindowEntity>> call, ObservableEmitter<BaseListResponse<HistoryPackWindowEntity>> emitter) {
        try {
            BaseListResponse<HistoryPackWindowEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }


    private void handleProductGroupResponse(Call<BaseListResponse<GroupEntity>> call, ObservableEmitter<BaseListResponse<GroupEntity>> emitter) {
        try {
            BaseListResponse<GroupEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleBaseResponse(Call<BaseResponse> call, ObservableEmitter<BaseResponse> emitter) {
        try {
            BaseResponse response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleIntegerResponse(Call<BaseResponse<Integer>> call, ObservableEmitter<BaseResponse<Integer>> emitter) {
        try {
            BaseResponse<Integer> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleStringResponse(Call<BaseResponse<String>> call, ObservableEmitter<BaseResponse<String>> emitter) {
        try {
            BaseResponse<String> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }
    private void handleModulePackagingResponse(Call<BaseListResponse<ListModuleEntity>> call, ObservableEmitter<BaseListResponse<ListModuleEntity>> emitter) {
        try {
            BaseListResponse<ListModuleEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }



    @Override
    public Observable<BaseListResponse<ProductEntity>> getInputForProductDetail(final long orderId, final int departmentId) {

        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<ProductEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<ProductEntity>> emitter) throws Exception {
                handleProductResponse(mRemoteApiInterface.getInputForProductDetail(
                        server + "/WS/api/GD2GetInputForProductDetail", orderId, departmentId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<ProductWindowEntity>> getInputForProductDetailWindow(final long orderId, final int departmentId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<ProductWindowEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<ProductWindowEntity>> emitter) throws Exception {
                handleProductWindowResponse(mRemoteApiInterface.getInputForProductDetailWindow(
                        server + "/WS/api/GD2GetInputForDetailCua", orderId, departmentId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<ProductWarehouseEntity>> getInputForProductWarehouse(final String key,final long orderId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<ProductWarehouseEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<ProductWarehouseEntity>> emitter) throws Exception {
                handleProductWarehouseResponse(mRemoteApiInterface.getInputForProductWarehouse(
                        server + "/WS/api/GD1GetProductInStore",key, orderId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse<String>> groupProductDetail(final String key, final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseResponse<String>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<String>> emitter) throws Exception {
                handleStringResponse(mRemoteApiInterface.groupProductDetail(
                        server + "/WS/api/GD2GroupProductDetail", key, json), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<GroupEntity>> getListProductDetailGroup(final long orderId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<GroupEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<GroupEntity>> emitter) throws Exception {
                handleProductGroupResponse(mRemoteApiInterface.getListProductDetailGroup(
                        server + "/WS/api/GD2GetListProductDetailGroup", orderId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<GroupEntity>> checkUpdateForGroup(final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<GroupEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<GroupEntity>> emitter) throws Exception {
                handleProductGroupResponse(mRemoteApiInterface.checkUpdateForGroup(
                        server + "/WS/api/GD2CheckUpdateForGroup", json), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse> deactiveProductDetailGroup(final String key, final long masterGroupId, final long userId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.deactiveProductDetailGroup(
                        server + "/WS/api/GD2DeactiveProductDetailGroup", key, masterGroupId, userId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse> updateProductDetailGroup(final String key, final long masterGroupId,
                                                             final String jsonNew, final String jsonUpdate,
                                                             final String jsonDelete, final long userId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.updateProductDetailGroup(
                        server + "/WS/api/GD2UpdateProductDetailGroup", key, masterGroupId,
                        jsonNew, jsonUpdate, jsonDelete, userId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse<Integer>> postListCodeProductDetail(final String key, final String json, final long userId, final String note) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");


        return Observable.create(new ObservableOnSubscribe<BaseResponse<Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<Integer>> emitter) throws Exception {
                handleIntegerResponse(mRemoteApiInterface.postListCodeProductDetail(
                        server + "/WS/api/GD2PostListCodeProductDetail", key, json,
                        userId, note), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse<Integer>> addScanTemHangCua(final String key, final long orderId, final long productSetId, final int direction, final String packCode, final int numberOnPack, final long userId, final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseResponse<Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<Integer>> emitter) throws Exception {
                handleIntegerResponse(mRemoteApiInterface.addScanTemHangCua(
                        server + "/WS/api/GD2AddScanTemHangCua", key,orderId,productSetId,
                        direction,packCode,numberOnPack,userId,json
                ), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<ListModuleEntity>> getListProductInPackage(final long orderId,final long apartmentId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<ListModuleEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<ListModuleEntity>> emitter) throws Exception {
                handleModulePackagingResponse(mRemoteApiInterface.getListProductInPackage(
                        server + "/WS/api/GD2GetListProductInPackage", orderId,  apartmentId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse<Integer>> addPhieuGiaoNhan(final String key, final long orderId, final int departmentInID,
                                                              final int departmentOutID, final int number, final String data, final long userId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseResponse<Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<Integer>> emitter) throws Exception {
                handleIntegerResponse(mRemoteApiInterface.addPhieuGiaoNhan(
                        server + "/WS/api/GD2AddPhieuGiaoNhan", key, orderId,departmentInID, departmentOutID, number,data,
                        userId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<ProductPackagingWindowEntity>> getProductSetDetailBySetAndDirec(final long productSetId, final int direction) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<ProductPackagingWindowEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<ProductPackagingWindowEntity>> emitter) throws Exception {
                handleProductPackingWindowResponse(mRemoteApiInterface.getProductSetDetailBySetAndDirec(
                        server + "/WS/api/GD2GetProductSetDetailBySetAndDirec",productSetId,direction), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<HistoryPackWindowEntity>> getHistoryIntemCua(final long productSetId, final int direction) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<HistoryPackWindowEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<HistoryPackWindowEntity>> emitter) throws Exception {
                handleHistoryPackWindowResponse(mRemoteApiInterface.getHistoryIntemCua(
                        server + "/WS/api/GD2GetHistoryIntemCua",productSetId,direction), emitter);
            }
        });
    }
}
