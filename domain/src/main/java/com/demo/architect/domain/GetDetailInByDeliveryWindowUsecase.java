package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.OrderConfirmWindowEntity;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.OrderConfirmWindowEntity;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class GetDetailInByDeliveryWindowUsecase extends BaseUseCase<BaseListResponse<OrderConfirmWindowEntity>> {
    private static final String TAG = GetDetailInByDeliveryWindowUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public GetDetailInByDeliveryWindowUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<OrderConfirmWindowEntity>> buildUseCaseObservable() {
        long maPhieuId = ((RequestValue) requestValues).maPhieuId;
        return remoteRepository.getDetailInByDeliveryWindow(maPhieuId);
    }


    @Override
    protected DisposableObserver<BaseListResponse<OrderConfirmWindowEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<OrderConfirmWindowEntity>>() {
            @Override
            public void onNext(BaseListResponse<OrderConfirmWindowEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                List<OrderConfirmWindowEntity> result = data.getData();
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
        private final long maPhieuId;

        public RequestValue(long maPhieuId) {
            this.maPhieuId = maPhieuId;
        }

    }

    public static final class ResponseValue implements ResponseValues {
        private List<OrderConfirmWindowEntity> entity;

        public ResponseValue(List<OrderConfirmWindowEntity> entity) {
            this.entity = entity;
        }

        public List<OrderConfirmWindowEntity> getEntity() {
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
