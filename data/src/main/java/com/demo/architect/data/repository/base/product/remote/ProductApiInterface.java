package com.demo.architect.data.repository.base.product.remote;


import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.HistoryPackWindowEntity;
import com.demo.architect.data.model.ListModuleEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.ProductPackagingWindowEntity;
import com.demo.architect.data.model.ProductWarehouseEntity;
import com.demo.architect.data.model.ProductWindowEntity;

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
    Call<BaseListResponse<ProductEntity>> getInputForProductDetail(@Url String url, @Field("pOderID") long orderId,
                                                                   @Field("pDepartmentID") int departmentID);
    @FormUrlEncoded
    @POST
    Call<BaseResponse<String>> groupProductDetail(@Url String url, @Field("pKey") String key,
                                                  @Field("pJsonListGroup") String json);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<GroupEntity>> getListProductDetailGroup(@Url String url, @Field("pOrderID") long orderId);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<GroupEntity>> checkUpdateForGroup(@Url String url, @Field("pJsonListGroup") String json);

    @FormUrlEncoded
    @POST
    Call<BaseResponse> deactiveProductDetailGroup(@Url String url, @Field("pKey") String key,
                                                  @Field("pMasterGroupID") long masterGroup,
                                                  @Field("pUserID") long userId);

    @FormUrlEncoded
    @POST
    Call<BaseResponse> updateProductDetailGroup(@Url String url, @Field("pKey") String key,
                                                @Field("pMasterGroupID") long masterGroup,
                                                @Field("pJsonNew") String jsonNew,
                                                @Field("pJsonUpdate") String jsonUpdate,
                                                @Field("pJsonDelete") String jsonDelete,
                                                @Field("pUserID") long userId);

    @FormUrlEncoded
    @POST
    Call<BaseResponse<Integer>> postListCodeProductDetail(@Url String url, @Field("pKey") String key,
                                                          @Field("pJsonListProductDetail") String json,
                                                          @Field("pUserID") long userId,
                                                          @Field("pNote") String note);


    @FormUrlEncoded
    @POST
    Call<BaseListResponse<ListModuleEntity>> getListProductInPackage(@Url String url, @Field("pOrderID") long orderId,
                                                                     @Field("pApartmentID") long apartmentId);

    @FormUrlEncoded
    @POST
    Call<BaseResponse<Integer>> addPhieuGiaoNhan(@Url String url, @Field("pKey") String key,
                                                 @Field("pOrderID") long orderId,
                                                 @Field("pDepartmentInID") int departmentInID,
                                                 @Field("pDepartmentOutID") int departmentOutID,
                                                 @Field("pNumber") int number,
                                                 @Field("pData") String data,
                                                 @Field("pUserID") long userId);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<ProductWindowEntity>> getInputForProductDetailWindow(@Url String url,
                                                                               @Field("pOrderID") long orderId,
                                                                               @Field("pDepartmentID") int departmentId);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<ProductWarehouseEntity>> getInputForProductWarehouse(@Url String url, @Field("pKey") String key,
                                                                               @Field("pOrderID") long orderId);


    @FormUrlEncoded
    @POST
    Call<BaseListResponse<ProductPackagingWindowEntity>> getProductSetDetailBySetAndDirec(@Url String url,
                                                                                          @Field("pProductSetID") long productSetId,
                                                                                          @Field("pDirec") int direction);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<HistoryPackWindowEntity>> getHistoryIntemCua(@Url String url,
                                                                       @Field("pProductSetID") long productSetId,
                                                                       @Field("pDirec") int direction);

    @FormUrlEncoded
    @POST
    Call<BaseResponse<Integer>> addScanTemHangCua(@Url String url, @Field("pKey") String key, @Field("pOrderID") long orderId,
                                                  @Field("pProductSetID") long productSetId,
                                                  @Field("pDirec") int direction,
                                                  @Field("pPackCode") String packCode,
                                                  @Field("pNumberSetOnPack") int numberOnPack,
                                                  @Field("pUserID") long userId,
                                                  @Field("pJson") String json);
}
