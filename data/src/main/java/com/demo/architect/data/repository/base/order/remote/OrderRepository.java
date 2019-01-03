package com.demo.architect.data.repository.base.order.remote;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.DeliveryNoteEntity;
import com.demo.architect.data.model.HistoryEntity;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.SOEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OrderRepository {
    Observable<BaseListResponse<SOEntity>> getListSO(int orderType);

    Observable<BaseListResponse<OrderConfirmEntity>> getInputUnConfirmed(long orderId, int departmentIDIn, int departmentIDOut);

    Observable<BaseResponse<Integer>> scanProductDetailOut(String key, String json);

    Observable<BaseListResponse> confirmInput(String key, int departmentId, String json);

    Observable<BaseListResponse<CodePackEntity>> getCodePack(long orderId, int orderType, long productId);

    Observable<BaseListResponse<ModuleEntity>> getModule(long orderId, int orderType, int departmentId);

    Observable<BaseListResponse<ProductPackagingEntity>> postCheckBarCode(long orderId, long productId,
                                                                          long apartmentId, String packCode,
                                                                          String sttPack, String code);

    Observable<BaseListResponse<HistoryEntity>> getListPrintPackageHistory(long orderId,
                                                                           long apartmentId);

    Observable<BaseListResponse<String>> getListModuleByOrder(long orderId);

    Observable<BaseListResponse<DeliveryNoteEntity>> getListMaPhieuGiao(String key, long orderId,
                                                                         int departmentIdIn,int departmentIdOut);


    Observable<BaseListResponse<OrderConfirmEntity>> getListInputUnConfirmByMaPhieu(long maPhieu);


}
