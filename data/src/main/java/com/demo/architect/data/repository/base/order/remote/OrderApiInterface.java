package com.demo.architect.data.repository.base.order.remote;


import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.DeliveryNoteEntity;
import com.demo.architect.data.model.HistoryEntity;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.OrderConfirmWindowEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.model.ReasonsEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.SOWarehouseEntity;

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
    Call<BaseListResponse<SOWarehouseEntity>> getListSOWarehouse(@Url String url, @Field("pKey") String key, @Field("pOrderType") int orderType);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<OrderConfirmEntity>> getInputUnConfirmed(@Url String url, @Field("pOrderID") long orderId,
                                                                   @Field("pDepartmentIDIn") int departmentIDIn,
                                                                   @Field("pDepartmentIDOut") int departmentIDOut);
    @FormUrlEncoded
    @POST
    Call<BaseResponse<Integer>> scanProductDetailOut(@Url String url, @Field("pKey") String key, @Field("pJsonProductDetailOut") String jsonProductDetailOut);


    @FormUrlEncoded
    @POST
    Call<BaseResponse<Integer>> scanProductDetailOutWindow(@Url String url, @Field("pKey") String key,
                                                           @Field("pOrderID") long orderId,
                                                           @Field("pDepartmentIDOut") int departmentOut,
                                                           @Field("pDepartmentIDIn") int departmentIn,
                                                           @Field("pUserID") long userId,
                                                           @Field("pStaffID") int staffId,
                                                           @Field("pJson") String jsonProductDetailOut);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse> confirmInput(@Url String url, @Field("pKey") String key, @Field("pDepartmentID") int departmentID, @Field("pJsonListInputComfirmed") String jsonListInputComfirmed);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<CodePackEntity>> getCodePack(@Url String url, @Field("pOrderID") long orderId,
                                                       @Field("pOrderType") int orderType,
                                                       @Field("pProductID") long productId);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<ModuleEntity>> getModule(@Url String url, @Field("pOrderID") long orderId,
                                                   @Field("pOrderType") int orderType,
                                                   @Field("pApartmentID") int departmentId);
    @FormUrlEncoded
    @POST
    Call<BaseListResponse<ProductPackagingEntity>> postCheckBarCode(@Url String url, @Field("pOrderID") long orderId,
                                                                    @Field("pProductID") long productId,
                                                                    @Field("pApartmentID") long apartmentId,
                                                                    @Field("pPackCode") String packCode,
                                                                    @Field("pSTTPack") String sttPack,
                                                                    @Field("pCode") String code);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<HistoryEntity>> getListPrintPackageHistory(@Url String url, @Field("pOrderID") long orderId,
                                                                     @Field("pApartmentID") long apartmentId);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<String>> getListModuleByOrder(@Url String url, @Field("pOrderID") long orderId);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<DeliveryNoteEntity>> getListMaPhieuGiao(@Url String url, @Field("pKey") String key, @Field("pOrderID") long orderId,
                                                                  @Field("pDepartmentIDIn") int departmentIdIn,
                                                                  @Field("pDepartmentIDOut") int departmentIdOut);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<OrderConfirmEntity>> getListInputUnConfirmByMaPhieu(@Url String url, @Field("pMaPhieuID") long maPhieuId);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<OrderConfirmWindowEntity>> getDetailInByDeliveryWindow(@Url String url, @Field("pMaPhieuID") long maPhieuId);
    @FormUrlEncoded
    @POST
    Call<BaseListResponse> confirmInputWindow(@Url String url, @Field("pKey") String key, @Field("pDepartmentID") int departmentId,
                                              @Field("pUserID") long userId, @Field("pJson") String json);

    @FormUrlEncoded
    @POST
    Call<BaseResponse> scanWarehousing(@Url String url, @Field("pKey") String key, @Field("pUserID") long userId,
                                       @Field("pOrderID") long orderId,
                                       @Field("pPhone") String phone, @Field("pDate") String date, @Field("pJson") String json);
}
