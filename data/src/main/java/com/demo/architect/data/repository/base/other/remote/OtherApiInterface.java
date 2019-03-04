package com.demo.architect.data.repository.base.other.remote;


import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.ListReasonsEntity;
import com.demo.architect.data.model.ReasonsEntity;
import com.demo.architect.data.model.TimesEntity;
import com.demo.architect.data.model.UploadEntity;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OtherApiInterface {
    @GET
    Call<BaseListResponse<DepartmentEntity>> getListDepartment(@Url String url);

    @GET
    Call<BaseResponse<ListReasonsEntity>> getRAndSQC(@Url String url);

    @FormUrlEncoded
    @POST
    Call<BaseResponse> addLogQC(@Url String url, @Field("pKey") String key,
                                @Field("pJsonListQC") String json);

    @FormUrlEncoded
    @POST
    Call<BaseResponse> addLogQCWindow(@Url String url, @Field("pKey") String key,
                                      @Field("pTenMay") String machineName,
                                      @Field("pNguoiViPham") String violator,
                                      @Field("pMaSoQC") String qcCode,
                                      @Field("pOrderID") long orderId,
                                      @Field("pDepartmentID") int departmentId,
                                      @Field("pUserID") long userId,
                                      @Field("pJson") String json);


    @Multipart
    @POST()
    Call<BaseResponse<UploadEntity>> uploadImage(
            @Url String url,
            @Part MultipartBody.Part file,
            @Part("pKey") RequestBody key,
            @Part("pOrderID") RequestBody orderId,
            @Part("pDepartmentID") RequestBody departmentId,
            @Part("pFileName") RequestBody fileName,
            @Part("pUserID") RequestBody userId);

    @FormUrlEncoded
    @POST
    Call<BaseListResponse<ApartmentEntity>> getApartment(@Url String url, @Field("pOrderID") long orderId);

    @FormUrlEncoded
    @POST
    Call<BaseResponse<TimesEntity>> getTimesInputAndOutputByDepartment(@Url String url, @Field("pOrderID") long orderId
            , @Field("pDepartmentID") int departmentId);

}
