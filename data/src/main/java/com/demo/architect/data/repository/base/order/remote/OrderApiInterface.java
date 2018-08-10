package com.demo.architect.data.repository.base.order.remote;


import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.ListCodeOutEntityResponse;
import com.demo.architect.data.model.OrderACRResponse;
import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.PackageEntity;
import com.demo.architect.data.model.ResultEntity;
import com.demo.architect.data.model.SOEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OrderApiInterface {
    @GET
    Call<BaseResponse<SOEntity>> getListSO(@Url String url,@Field("pOrderType") int orderType);

    @POST
    Call<BaseResponse> scanProductDetailOut(@Url String url,@Field("pJsonProductDetailOut") String jsonProductDetailOut);

}
