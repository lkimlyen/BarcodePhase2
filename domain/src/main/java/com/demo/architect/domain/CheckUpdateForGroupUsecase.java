package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class CheckUpdateForGroupUsecase extends BaseUseCase {
    private static final String TAG = CheckUpdateForGroupUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public CheckUpdateForGroupUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        String json = ((RequestValue) requestValues).json;
        return remoteRepository.checkUpdateForGroup(json);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<BaseListResponse<GroupEntity>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.toString());
                if (useCaseCallback != null) {
                    useCaseCallback.onError(new ErrorValue(e.toString(), null));
                }
            }

            @Override
            public void onNext(BaseListResponse<GroupEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<GroupEntity> result = data.getData();
                    if (result != null && data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue());
                    } else {
                        useCaseCallback.onError(new ErrorValue(data.getDescription(), result));
                    }
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
