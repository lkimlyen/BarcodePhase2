package com.demo.architect.data.repository.base.product.remote;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public interface ProductRepository {
    Observable<BaseListResponse<ProductEntity>> getInputForProductDetail(int orderId,
                                                                         int departmentId);

    Observable<BaseResponse<String>> groupProductDetail(String key, String json);

    Observable<BaseListResponse<ProductGroupEntity>> getListProductDetailGroup(int orderId);

    Observable<BaseResponse> deactiveProductDetailGroup(String key, String groupCode);

}
