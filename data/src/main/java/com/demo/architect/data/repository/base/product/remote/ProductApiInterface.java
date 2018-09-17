package com.demo.architect.data.repository.base.product.remote;


import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.ListModuleEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.ProductPackagingEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by Skull on 04/01/2018.
 */

public interface ProductApiInterface {
    @FormUrlEncoded
    @POST
    Call<BaseListResponse<ProductEntity>> getInputForProductDetail(@Url String url, @Field("pOderID") int orderId,
                                                                   @Field("pDepartmentID") int departmentID);
    @FormUrlEncoded
    @POST
    Call<BaseResponse<String>> groupProductDetail(@Url String url, @Field("pKey") String key,
                                                               @Field("pJsonListGroup") String json);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<ProductGroupEntity>> getListProductDetailGroup(@Url String url, @Field("pOrderID") int orderId);

    @FormUrlEncoded
    @POST
    Call<BaseResponse> deactiveProductDetailGroup(@Url String url, @Field("pKey") String key,
                                                  @Field("pGroupCode") String groupCode,
                                                  @Field("pUserID") int userId);

    @FormUrlEncoded
    @POST
    Call<BaseResponse> updateProductDetailGroup(@Url String url, @Field("pKey") String key,
                                                  @Field("pGroupCode") String groupCode,
                                                @Field("pJsonNew") String jsonNew,
                                                @Field("pJsonUpdate") String jsonUpdate,
                                                @Field("pJsonDelete") String jsonDelete,
                                                @Field("pUserID") int userId);

    @FormUrlEncoded
    @POST
    Call<BaseResponse<Integer>> postListCodeProductDetail(@Url String url, @Field("pKey") String key,
                                                @Field("pJsonListProductDetail") String json,
                                                @Field("pUserID") int userId,
                                                @Field("pNote") String note);


    @FormUrlEncoded
    @POST
    Call<BaseListResponse<ListModuleEntity>> getListProductInPackage(@Url String url, @Field("pOrderID") int orderId,
                                                                     @Field("pApartmentID") int apartmentId);

}
