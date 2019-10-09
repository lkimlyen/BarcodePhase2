package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class CheckUpdateForGroupUsecase extends BaseUseCase<BaseListResponse<GroupEntity>> {
    private static final String TAG = CheckUpdateForGroupUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public CheckUpdateForGroupUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse<GroupEntity>> buildUseCaseObservable() {
        String json = ((RequestValue) requestValues).json;
        return remoteRepository.checkUpdateForGroup(json);
    }


    @Override
    protected DisposableObserver<BaseListResponse<GroupEntity>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse<GroupEntity>>() {
            @Override
            public void onNext(BaseListResponse<GroupEntity>data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    List<GroupEntity> result = data.getData();
                    if (result != null && data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue());
                    } else {
                        useCaseCallback.onError(new ErrorValue(data.getDescription(), result));
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
                    useCaseCallback.onError(new ErrorValue(e.getMessage(), new ArrayList<GroupEntity>()));
                }
            }
        };
    }
    public static final class RequestValue implements RequestValues {
        private final String json;

        public RequestValue(String json) {
            this.json = json;
        }
    }

    public static final class ResponseValue implements ResponseValues {

    }

    public static final class ErrorValue implements ErrorValues {
        private String description;
        private List<GroupEntity> entity;

        public ErrorValue(String description, List<GroupEntity> entity) {
            this.description = description;
            this.entity = entity;
        }

        public String getDescription() {
            return description;
        }

        public List<GroupEntity> getEntity() {
            return entity;
        }
    }
}
