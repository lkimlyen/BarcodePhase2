package com.demo.architect.data.repository.base.order.remote;

import android.content.Context;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.DeliveryNoteEntity;
import com.demo.architect.data.model.HistoryEntity;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.OrderConfirmWindowEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.SOWarehouseEntity;
import com.demo.architect.data.model.UserEntity;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import retrofit2.Call;

import io.reactivex.Observable;

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
    private void handleSOResponse(Call<BaseListResponse<SOEntity>> call, ObservableEmitter<BaseListResponse<SOEntity>> emitter) {
        try {
            BaseListResponse<SOEntity> response = call.execute().body();

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
    private void handleSOWarehouseResponse(Call<BaseListResponse<SOWarehouseEntity>> call, ObservableEmitter<BaseListResponse<SOWarehouseEntity>> emitter) {
        try {
            BaseListResponse<SOWarehouseEntity> response = call.execute().body();

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

    private void handleOrderConfirmResponse(Call<BaseListResponse<OrderConfirmEntity>> call, ObservableEmitter<BaseListResponse<OrderConfirmEntity>> emitter) {
        try {
            BaseListResponse<OrderConfirmEntity> response = call.execute().body();

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
    

    private void handleOrderConfirmWindowResponse(Call<BaseListResponse<OrderConfirmWindowEntity>> call, ObservableEmitter<BaseListResponse<OrderConfirmWindowEntity>> emitter) {
        try {
            BaseListResponse<OrderConfirmWindowEntity> response = call.execute().body();

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
    private void handleListStringResponse(Call<BaseListResponse<String>> call, ObservableEmitter<BaseListResponse<String>> emitter) {
        try {
            BaseListResponse<String> response = call.execute().body();

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
   
    private void handleBaseListResponse(Call<BaseListResponse> call, ObservableEmitter<BaseListResponse> emitter) {
        try {
            BaseListResponse response = call.execute().body();

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

    private void handleCodePackResponse(Call<BaseListResponse<CodePackEntity>> call, ObservableEmitter<BaseListResponse<CodePackEntity>> emitter) {
        try {
            BaseListResponse<CodePackEntity> response = call.execute().body();

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
    private void handleModuleResponse(Call<BaseListResponse<ModuleEntity>> call, ObservableEmitter<BaseListResponse<ModuleEntity>> emitter) {
        try {
            BaseListResponse<ModuleEntity> response = call.execute().body();

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
    private void handleDeliveryNoteResponse(Call<BaseListResponse<DeliveryNoteEntity>> call, ObservableEmitter<BaseListResponse<DeliveryNoteEntity>> emitter) {
        try {
            BaseListResponse<DeliveryNoteEntity> response = call.execute().body();

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
    private void handleProductPackagingResponse(Call<BaseListResponse<ProductPackagingEntity>> call, ObservableEmitter<BaseListResponse<ProductPackagingEntity>> emitter) {
        try {
            BaseListResponse<ProductPackagingEntity> response = call.execute().body();

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

    private void handleHistoryResponse(Call<BaseListResponse<HistoryEntity>> call, ObservableEmitter<BaseListResponse<HistoryEntity>> emitter) {
        try {
            BaseListResponse<HistoryEntity> response = call.execute().body();

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
    public Observable<BaseListResponse<SOEntity>> getListSO(final int orderType) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<SOEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<SOEntity>> emitter) throws Exception {
                handleSOResponse(mRemoteApiInterface.getListSO(
                        server + "/WS/api/GD2GetListSO", orderType), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<SOWarehouseEntity>> getListSOWarehouse(final String key, final int orderType) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");


        return Observable.create(new ObservableOnSubscribe<BaseListResponse<SOWarehouseEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<SOWarehouseEntity>> emitter) throws Exception {
                handleSOWarehouseResponse(mRemoteApiInterface.getListSOWarehouse(
                        server + "/WS/api/GD1GetListSO", key,orderType), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<OrderConfirmEntity>> getInputUnConfirmed(final long orderId, final int departmentIDIn, final int departmentIDOut) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<OrderConfirmEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<OrderConfirmEntity>> emitter) throws Exception {
                handleOrderConfirmResponse(mRemoteApiInterface.getInputUnConfirmed(
                        server + "/WS/api/GD2GetInputUnConfirmed", orderId, departmentIDIn, departmentIDOut), emitter);
            }
        });

    }

    @Override
    public Observable<BaseResponse<Integer>> scanProductDetailOut(final String key, final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseResponse<Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<Integer>> emitter) throws Exception {
                handleIntegerResponse(mRemoteApiInterface.scanProductDetailOut(
                        server + "/WS/api/GD2ScanProductDetailOut", key, json), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse<Integer>> scanProductDetailOutWindow(final String key, final long orderId, final int departmentOut,
                                                                        final int departmentIn, final long userId, final int staffId, final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseResponse<Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<Integer>> emitter) throws Exception {
                handleIntegerResponse(mRemoteApiInterface.scanProductDetailOutWindow(
                        server + "/WS/api/GD2ScanDetailOutCua", key, orderId,departmentOut,departmentIn,userId,staffId,json), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse> confirmInput(final String key, final int departmentId, final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse> emitter) throws Exception {
                handleBaseListResponse(mRemoteApiInterface.confirmInput(
                        server + "/WS/api/GD2ComfirmInputByMaPhieu", key, departmentId, json), emitter);
            }
        });

    }

    @Override
    public Observable<BaseListResponse> confirmInputWindow(final String key, final int departmentId, final long userId, final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse> emitter) throws Exception {
                handleBaseListResponse(mRemoteApiInterface.confirmInputWindow(
                        server + "/WS/api/GD2ConfirmDetailInByMaPhieuCua", key, departmentId,userId, json), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<CodePackEntity>> getCodePack(final long orderId, final int orderType, final long productId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<CodePackEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<CodePackEntity>> emitter) throws Exception {
                handleCodePackResponse(mRemoteApiInterface.getCodePack(
                        server + "/WS/api/GD2GetCodePack", orderId, orderType, productId), emitter);
            }
        });

    }

    @Override
    public Observable<BaseListResponse<ModuleEntity>> getModule(final long orderId, final int orderType, final int departmentId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<ModuleEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<ModuleEntity>> emitter) throws Exception {
                handleModuleResponse(mRemoteApiInterface.getModule(
                        server + "/WS/api/GD2GetModule", orderId, orderType, departmentId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<ProductPackagingEntity>> postCheckBarCode(final long orderId, final long productId, final long apartmentId, final String packCode, final String sttPack, final String code) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");
       return Observable.create(new ObservableOnSubscribe<BaseListResponse<ProductPackagingEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<ProductPackagingEntity>> emitter) throws Exception {
                handleProductPackagingResponse(mRemoteApiInterface.postCheckBarCode(
                        server + "/WS/api/GD2PostCheckBarCode", orderId, productId, apartmentId,
                        packCode, sttPack, code), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<HistoryEntity>> getListPrintPackageHistory(final long orderId, final long apartmentId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");


        return Observable.create(new ObservableOnSubscribe<BaseListResponse<HistoryEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<HistoryEntity>> emitter) throws Exception {
                handleHistoryResponse(mRemoteApiInterface.getListPrintPackageHistory(
                        server + "/WS/api/GD2GetListPrintPackageHistory", orderId, apartmentId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<String>> getListModuleByOrder(final long orderId) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");


        return Observable.create(new ObservableOnSubscribe<BaseListResponse<String>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<String>> emitter) throws Exception {
                handleListStringResponse(mRemoteApiInterface.getListModuleByOrder(
                        server + "/WS/api/GD2GetListModuleByOrder", orderId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<DeliveryNoteEntity>> getListMaPhieuGiao(final String key, final long orderId, final int  departmentIdIn, final int departmentIdOut) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");


        return Observable.create(new ObservableOnSubscribe<BaseListResponse<DeliveryNoteEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<DeliveryNoteEntity>> emitter) throws Exception {
                handleDeliveryNoteResponse(mRemoteApiInterface.getListMaPhieuGiao(
                        server + "/WS/api/GD2GetListMaPhieuGiao", key,orderId, departmentIdIn,departmentIdOut), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<OrderConfirmEntity>> getListInputUnConfirmByMaPhieu(final long maPhieu) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<OrderConfirmEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<OrderConfirmEntity>> emitter) throws Exception {
                handleOrderConfirmResponse(mRemoteApiInterface.getListInputUnConfirmByMaPhieu(
                        server + "/WS/api/GD2GetListInputUnConfirmByMaPhieu", maPhieu), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<OrderConfirmWindowEntity>> getDetailInByDeliveryWindow(final long maPhieu) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseListResponse<OrderConfirmWindowEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<OrderConfirmWindowEntity>> emitter) throws Exception {
                handleOrderConfirmWindowResponse(mRemoteApiInterface.getDetailInByDeliveryWindow(
                        server + "/WS/api/GD2GetDetailInByMaPhieuCua", maPhieu), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse> scanWarehousing(final String key, final long userId, final long orderId, final String phone, final String date, final String json) {
        server = SharedPreferenceHelper.getInstance(context).getString(Constants.KEY_SERVER, "");

        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.scanWarehousing(
                        server + "/WS/api/GD1ScanProductInStore",key, userId,orderId,phone,date,json), emitter);
            }
        });
    }
}
