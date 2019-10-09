package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class GetInputForProductDetailUsecase extends BaseUseCase<BaseListResponse<ProductEntity>> {
    private static final String TAG = GetInputForProductDetailUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public GetInputForProductDetailUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<ProductEntity>> buildUseCaseObservable() {
        long orderId = ((RequestValue) requestValues).orderId;
        int departmentId = ((RequestValue) requestValues).departmentId;
        return remoteRepository.getInputForProductDetail(orderId, departmentId);
    }
    @Override
    protected DisposableObserver<BaseListResponse<ProductEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<ProductEntity>>() {
            @Override
            public void onNext(BaseListResponse<ProductEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                List<ProductEntity> result = data.getData();
                if (result != null && data.getStatus() == 1) {
                    useCaseCallback.onSuccess(new ResponseValue(result));
                } else {
                    useCaseCallback.onError(new ErrorValue(data.getDescription()));
                }
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.toString());
                if (useCaseCallback != null) {
                    useCaseCallback.onError(new ErrorValue(e.toString()));
                }
            }
        };
    }


    public static final class RequestValue implements RequestValues {
        private final long orderId;
        private final int departmentId;

        public RequestValue(long orderId, int departmentId) {
            this.orderId = orderId;
            this.departmentId = departmentId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private List<ProductEntity> entity;

        public ResponseValue(List<ProductEntity> entity) {
            this.entity = entity;
        }

        public List<ProductEntity> getEntity() {
            return entity;
        }
    }

    public static final class ErrorValue implements ErrorValues {
        private String description;

        public ErrorValue(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
