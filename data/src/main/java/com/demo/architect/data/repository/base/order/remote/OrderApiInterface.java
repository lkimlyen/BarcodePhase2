package com.demo.architect.data.repository.base.order.remote;


import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.SOEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OrderApiInterface {
    @FormUrlEncoded
    @POST
    Call<BaseListResponse<SOEntity>> getListSO(@Url String url, @Field("pOrderType") int orderType);
    @FormUrlEncoded
    @POST
    Call<BaseListResponse<OrderConfirmEntity>> getInputUnConfirmed(@Url String url, @Field("pOrderID") int orderId,
                                                                   @Field("pDepartmentIDIn") int departmentIDIn,
                                                                   @Field("pDepartmentIDOut") int departmentIDOut);
    @FormUrlEncoded
    @POST
    Call<BaseListResponse> scanProductDetailOut(@Url String url,@Field("pKey") String key, @Field("pJsonProductDetailOut") String jsonProductDetailOut);
    @FormUrlEncoded
    @POST
    Call<BaseListResponse> confirmInput(@Url String url, @Field("pDepartmentID") int departmentID, @Field("pJsonListInputComfirmed") String jsonListInputComfirmed);

}
