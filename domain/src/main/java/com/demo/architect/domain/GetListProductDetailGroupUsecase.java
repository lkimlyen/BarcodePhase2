package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class GetListProductDetailGroupUsecase extends BaseUseCase<BaseListResponse<GroupEntity>> {
    private static final String TAG = GetListProductDetailGroupUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public GetListProductDetailGroupUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<GroupEntity>> buildUseCaseObservable() {
        long orderId = ((RequestValue) requestValues).orderId;
        return remoteRepository.getListProductDetailGroup(orderId);
    }

    @Override
    protected DisposableObserver<BaseListResponse<GroupEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<GroupEntity>>() {
            @Override
            public void onNext(BaseListResponse<GroupEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    List<GroupEntity> result = data.getData();
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
        private List<GroupEntity> entity;

        public ResponseValue(List<GroupEntity> entity) {
            this.entity = entity;
        }

        public List<GroupEntity> getEntity() {
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
