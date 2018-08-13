package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class GetListSOUsecase extends BaseUseCase {
    private static final String TAG = GetListSOUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public GetListSOUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        int orderType = ((RequestValue) requestValues).orderType;
        return remoteRepository.getListSO(orderType);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<BaseListResponse<SOEntity>>() {
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
            public void onNext(BaseListResponse<SOEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<SOEntity> result = data.getData();
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
        private final int orderType;

        public RequestValue(int orderType) {
            this.orderType = orderType;
        }

    }

    public static final class ResponseValue implements ResponseValues {
        private List<SOEntity> entity;

        public ResponseValue(List<SOEntity> entity) {
            this.entity = entity;
        }

        public List<SOEntity> getEntity() {
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
