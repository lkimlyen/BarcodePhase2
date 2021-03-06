package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class GetListModuleByOrderUsecase extends BaseUseCase<BaseListResponse<String>> {
    private static final String TAG = GetListModuleByOrderUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public GetListModuleByOrderUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<String>> buildUseCaseObservable() {
        long orderId = ((RequestValue) requestValues).orderId;
        return remoteRepository.getListModuleByOrder(orderId);
    }

    @Override
    protected DisposableObserver<BaseListResponse<String>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<String>>() {
            @Override
            public void onNext(BaseListResponse<String> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    List<String> result = data.getData();
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
        private List<String> entity;

        public ResponseValue(List<String> entity) {
            this.entity = entity;
        }

        public List<String> getEntity() {
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
