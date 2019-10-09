package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.CodePackEntity;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class GetModuleUsecase extends BaseUseCase<BaseListResponse<ModuleEntity>> {
    private static final String TAG = GetModuleUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public GetModuleUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<ModuleEntity>> buildUseCaseObservable() {
        long orderId = ((RequestValue) requestValues).orderId;
        int orderType = ((RequestValue) requestValues).orderType;
        int apartmentId = ((RequestValue) requestValues).apartmentId;
        return remoteRepository.getModule(orderId, orderType, apartmentId);
    }

    @Override
    protected DisposableObserver<BaseListResponse<ModuleEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<ModuleEntity>>() {
            @Override
            public void onNext(BaseListResponse<ModuleEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    List<ModuleEntity> result = data.getData();
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
                    useCaseCallback.onError(new ErrorValue(e.getMessage()));
                }
            }
        };
    }

    public static final class RequestValue implements RequestValues {
        private final long orderId;
        private final int orderType;
        private final int apartmentId;

        public RequestValue(long orderId, int departmentIdIn, int apartmentId) {
            this.orderId = orderId;
            this.orderType = departmentIdIn;
            this.apartmentId = apartmentId;
        }

    }

    public static final class ResponseValue implements ResponseValues {
        private List<ModuleEntity> entity;

        public ResponseValue(List<ModuleEntity> entity) {
            this.entity = entity;
        }

        public List<ModuleEntity> getEntity() {
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
