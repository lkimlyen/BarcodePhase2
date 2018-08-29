package com.demo.architect.data.repository.base.order.remote;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.SOEntity;

import rx.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OrderRepository {
    Observable<BaseListResponse<SOEntity>> getListSO(int orderType);

    Observable<BaseListResponse<OrderConfirmEntity>> getInputUnConfirmed(int orderId, int departmentIDIn, int departmentIDOut);

    Observable<BaseListResponse> scanProductDetailOut(String key, String json);

    Observable<BaseListResponse> confirmInput(String key, int departmentId, String json);

    Observable<BaseListResponse<CodePackEntity>> getCodePack(int orderId, int orderType, int productId);

    Observable<BaseListResponse<ModuleEntity>> getModule(int orderId, int orderType, int departmentId);

    Observable<BaseListResponse<ProductPackagingEntity>> postCheckBarCode(int orderId, int productId,
                                                                          int apartmentId,String packCode,
                                                                          String sttPack,String code);
}
