package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class GetCodePackUsecase extends BaseUseCase <BaseListResponse<CodePackEntity>>{
    private static final String TAG = GetCodePackUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public GetCodePackUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<CodePackEntity>> buildUseCaseObservable() {
        long orderId = ((RequestValue) requestValues).orderId;
        int orderType = ((RequestValue) requestValues).orderType;
        long productId = ((RequestValue) requestValues).productId;
        return remoteRepository.getCodePack(orderId, orderType, productId);
    }


    @Override
    protected DisposableObserver<BaseListResponse<CodePackEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<CodePackEntity>>() {
            @Override
            public void onNext(BaseListResponse<CodePackEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                List<CodePackEntity> result = data.getData();
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
        private final int orderType;
        private final long productId;

        public RequestValue(long orderId, int departmentIdIn, long productId) {
            this.orderId = orderId;
            this.orderType = departmentIdIn;
            this.productId = productId;
        }

    }

    public static final class ResponseValue implements ResponseValues {
        private List<CodePackEntity> entity;

        public ResponseValue(List<CodePackEntity> entity) {
            this.entity = entity;
        }

        public List<CodePackEntity> getEntity() {
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
