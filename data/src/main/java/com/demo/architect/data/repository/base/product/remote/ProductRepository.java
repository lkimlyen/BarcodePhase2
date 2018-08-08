package com.demo.architect.data.repository.base.product.remote;

import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.ProductEntity;

import rx.Observable;

/**
 * Created by Skull on 04/01/2018.
 */

public interface ProductRepository {
    Observable<BaseResponse<ProductEntity>> getInputForProductDetail(int orderId,
                                                                     int departmentId);
}
