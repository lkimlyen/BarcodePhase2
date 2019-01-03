package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class GetListInputUnConfirmByMaPhieuUsecase extends BaseUseCase {
    private static final String TAG = GetListInputUnConfirmByMaPhieuUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public GetListInputUnConfirmByMaPhieuUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        long maPhieuId = ((RequestValue) requestValues).maPhieuId;
        return remoteRepository.getListInputUnConfirmByMaPhieu(maPhieuId);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<BaseListResponse<OrderConfirmEntity>>() {
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
            public void onNext(BaseListResponse<OrderConfirmEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<OrderConfirmEntity> result = data.getData();
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
        private final long maPhieuId;

        public RequestValue(long maPhieuId) {
            this.maPhieuId = maPhieuId;
        }

    }

    public static final class ResponseValue implements ResponseValues {
        private List<OrderConfirmEntity> entity;

        public ResponseValue(List<OrderConfirmEntity> entity) {
            this.entity = entity;
        }

        public List<OrderConfirmEntity> getEntity() {
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
