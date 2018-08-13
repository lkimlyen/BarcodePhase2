package com.demo.architect.data.repository.base.other.remote;


import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.DepartmentEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Skull on 04/01/2018.
 */

public interface OtherApiInterface {
    @GET
    Call<BaseListResponse<DepartmentEntity>> getListDepartment(@Url String url);
}
