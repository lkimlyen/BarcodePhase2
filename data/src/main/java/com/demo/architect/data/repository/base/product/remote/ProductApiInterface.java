package com.demo.architect.data.repository.base.product.remote;


import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.ProductEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Skull on 04/01/2018.
 */

public interface ProductApiInterface {

    @GET
    Call<BaseListResponse<ProductEntity>> getInputForProductDetail(@Url String url, @Field("pOderID") int orderId,
                                                                   @Field("pDepartmentID") int departmentID);

}
