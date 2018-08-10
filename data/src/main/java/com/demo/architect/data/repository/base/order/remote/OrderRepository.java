package com.demo.architect.data.repository.base.order.remote;

import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.ListCodeOutEntityResponse;
import com.demo.architect.data.model.OrderACRResponse;
import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ResultEntity;
import com.demo.architect.data.model.SOEntity;

import rx.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OrderRepository {
    Observable<BaseResponse<SOEntity>> getListSO(int orderType);
    Observable<BaseResponse> scanProductDetailOut(String json);

}
