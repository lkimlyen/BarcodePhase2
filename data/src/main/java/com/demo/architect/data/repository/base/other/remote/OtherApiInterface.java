package com.demo.architect.data.repository.base.other.remote;


import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.DepartmentEntity;
import com.demo.architect.data.model.ListCodeOutEntityResponse;
import com.demo.architect.data.model.OrderACRResponse;
import com.demo.architect.data.model.OrderRequestEntity;
import com.demo.architect.data.model.ResultEntity;

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

public interface OtherApiInterface {
    @GET
    Call<BaseResponse<DepartmentEntity>> getListDepartment(@Url String url);

    @GET
    Call<BaseResponse<DepartmentEntity>> getListDepartment(@Url String url);

}
