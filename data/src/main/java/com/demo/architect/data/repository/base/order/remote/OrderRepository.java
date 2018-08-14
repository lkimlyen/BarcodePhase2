package com.demo.architect.data.repository.base.order.remote;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.SOEntity;

import rx.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OrderRepository {
    Observable<BaseListResponse<SOEntity>> getListSO(int orderType);

    Observable<BaseListResponse<OrderConfirmEntity>> getInputUnConfirmed(int orderId, int departmentIDIn, int departmentIDOut);

    Observable<BaseListResponse> scanProductDetailOut(String json);

    Observable<BaseListResponse> confirmInput(int departmentId, String json);

}
