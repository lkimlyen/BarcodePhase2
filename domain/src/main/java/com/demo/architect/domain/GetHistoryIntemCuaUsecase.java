package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.HistoryPackWindowEntity;
import com.demo.architect.data.model.HistoryPackWindowEntity;
import com.demo.architect.data.model.ProductPackagingWindowEntity;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class GetHistoryIntemCuaUsecase extends BaseUseCase<BaseListResponse<HistoryPackWindowEntity>> {
    private static final String TAG = GetHistoryIntemCuaUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public GetHistoryIntemCuaUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<HistoryPackWindowEntity>> buildUseCaseObservable() {
        long productSetId = ((RequestValue) requestValues).productSetId;
        int direction = ((RequestValue) requestValues).direction;
        return remoteRepository.getHistoryIntemCua(productSetId, direction);
    }

    @Override
    protected DisposableObserver<BaseListResponse<HistoryPackWindowEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<HistoryPackWindowEntity>>() {
            @Override
            public void onNext(BaseListResponse<HistoryPackWindowEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                List<HistoryPackWindowEntity> result = data.getData();
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
        private final long productSetId;
        private final int direction;

        public RequestValue(long productSetId, int direction) {
            this.productSetId = productSetId;
            this.direction = direction;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private List<HistoryPackWindowEntity> entity;

        public ResponseValue(List<HistoryPackWindowEntity> entity) {
            this.entity = entity;
        }

        public List<HistoryPackWindowEntity> getEntity() {
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
