package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.ProductWarehouseEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductWarehouseEntity;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class GetInputForProductWarehouseUsecase extends BaseUseCase<BaseListResponse<ProductWarehouseEntity>> {
    private static final String TAG = GetInputForProductWarehouseUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public GetInputForProductWarehouseUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<ProductWarehouseEntity>> buildUseCaseObservable() {
        long orderId = ((RequestValue) requestValues).orderId;
        return remoteRepository.getInputForProductWarehouse("13AKby8uFhdlayHD6oPsaU90b8o00=",orderId);
    }


    @Override
    protected DisposableObserver<BaseListResponse<ProductWarehouseEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<ProductWarehouseEntity>>() {
            @Override
            public void onNext(BaseListResponse<ProductWarehouseEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    List<ProductWarehouseEntity> result = data.getData();
                    if (result != null && data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue(result));
                    } else {
                        useCaseCallback.onError(new ErrorValue(data.getDescription()));
                    }
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

        public RequestValue(long orderId) {
            this.orderId = orderId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private List<ProductWarehouseEntity> entity;

        public ResponseValue(List<ProductWarehouseEntity> entity) {
            this.entity = entity;
        }

        public List<ProductWarehouseEntity> getEntity() {
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
