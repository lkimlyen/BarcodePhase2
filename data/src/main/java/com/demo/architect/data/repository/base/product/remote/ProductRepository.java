package com.demo.architect.data.repository.base.product.remote;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ListModuleEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.ProductPackagingEntity;

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
    Observable<BaseListResponse<ProductEntity>> getInputForProductDetail(long orderId,
                                                                         int departmentId);

    Observable<BaseResponse<String>> groupProductDetail(String key, String json);

    Observable<BaseListResponse<GroupEntity>> getListProductDetailGroup(long orderId);

    Observable<BaseListResponse<GroupEntity>> checkUpdateForGroup(String json);

    Observable<BaseResponse> deactiveProductDetailGroup(String key, long masterGroupId, long userId);

    Observable<BaseResponse> updateProductDetailGroup(String key,  long masterGroupId,
                                                      String jsonNew,String jsonUpdate,
                                                      String jsonDelete,long userId);

    Observable<BaseResponse<Integer>> postListCodeProductDetail(String key, String json,
                                                      long userId,String note);
    Observable<BaseListResponse<ListModuleEntity>> getListProductInPackage(long orderId,
                                                                           long apartmentId);



}
