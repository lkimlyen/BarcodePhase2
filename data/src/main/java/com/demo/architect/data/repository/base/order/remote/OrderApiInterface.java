package com.demo.architect.data.repository.base.order.remote;


import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.HistoryEntity;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.ReasonsEntity;
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
    Call<BaseListResponse> confirmInput(@Url String url,@Field("pKey") String key, @Field("pDepartmentID") int departmentID, @Field("pJsonListInputComfirmed") String jsonListInputComfirmed);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<CodePackEntity>> getCodePack(@Url String url, @Field("pOrderID") int orderId,
                                                       @Field("pOrderType") int orderType,
                                                       @Field("pProductID") int productId);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<ModuleEntity>> getModule(@Url String url, @Field("pOrderID") int orderId,
                                                   @Field("pOrderType") int orderType,
                                                   @Field("pApartmentID") int departmentId);
    @FormUrlEncoded
    @POST
    Call<BaseListResponse<ProductPackagingEntity>> postCheckBarCode(@Url String url, @Field("pOrderID") int orderId,
                                                                    @Field("pProductID") int productId,
                                                                    @Field("pApartmentID") int apartmentId,
                                                                    @Field("pPackCode") String packCode,
                                                                    @Field("pSTTPack") String sttPack,
                                                                    @Field("pCode") String code);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<HistoryEntity>> getListPrintPackageHistory(@Url String url, @Field("pOrderID") int orderId,
                                                           @Field("pProductID") int productId,
                                                           @Field("pApartmentID") int apartmentId,
                                                           @Field("pCodePack") String packCode,
                                                           @Field("pSTTPack") String sttPack);

}
