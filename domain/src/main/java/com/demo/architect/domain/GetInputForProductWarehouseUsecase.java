package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductWarehouseEntity;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class GetInputForProductWarehouseUsecase extends BaseUseCase {
    private static final String TAG = GetInputForProductWarehouseUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public GetInputForProductWarehouseUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        long orderId = ((RequestValue) requestValues).orderId;
        return remoteRepository.getInputForProductWarehouse("13AKby8uFhdlayHD6oPsaU90b8o00=",orderId);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<BaseListResponse<ProductWarehouseEntity>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.toString());
                if (useCaseCallback != null) {
                    useCaseCallback.onError(new ErrorValue(e.toString()));
                }
            }

            @Override
            public void onNext(BaseListResponse<ProductWarehouseEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<ProductWarehouseEntity> result = data.getData();
                    if (result != null && data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue(result));
                    } else {
                        useCaseCallback.onError(new ErrorValue(data.getDescription()));
                    }
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
