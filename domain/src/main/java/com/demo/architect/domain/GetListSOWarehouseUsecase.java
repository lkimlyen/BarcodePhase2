package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.SOWarehouseEntity;
import com.demo.architect.data.model.SOEntity;
import com.demo.architect.data.model.SOWarehouseEntity;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class GetListSOWarehouseUsecase extends BaseUseCase<BaseListResponse<SOWarehouseEntity>> {
    private static final String TAG = GetListSOWarehouseUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public GetListSOWarehouseUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<SOWarehouseEntity>> buildUseCaseObservable() {
        int orderType = ((RequestValue) requestValues).orderType;
        return remoteRepository.getListSOWarehouse("13AKby8uFhdlayHD6oPsaU90b8o00=",orderType);
    }


    @Override
    protected DisposableObserver<BaseListResponse<SOWarehouseEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<SOWarehouseEntity>>() {
            @Override
            public void onNext(BaseListResponse<SOWarehouseEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    List<SOWarehouseEntity> result = data.getData();
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
        private final int orderType;

        public RequestValue(int orderType) {
            this.orderType = orderType;
        }

    }

    public static final class ResponseValue implements ResponseValues {
        private List<SOWarehouseEntity> entity;

        public ResponseValue(List<SOWarehouseEntity> entity) {
            this.entity = entity;
        }

        public List<SOWarehouseEntity> getEntity() {
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
