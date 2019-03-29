package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.SetWindowEntity;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class GetProductSetUsecase extends BaseUseCase {
    private static final String TAG = GetProductSetUsecase.class.getSimpleName();
    private final OtherRepository remoteRepository;

    public GetProductSetUsecase(OtherRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        long orderId = ((RequestValue) requestValues).orderId;
        return remoteRepository.getProductSet(orderId);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<BaseListResponse<SetWindowEntity>>() {
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
            public void onNext(BaseListResponse<SetWindowEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<SetWindowEntity> result = data.getData();
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
        private final long orderId;

        public RequestValue(long orderId) {
            this.orderId = orderId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private List<SetWindowEntity> entity;

        public ResponseValue(List<SetWindowEntity> entity) {
            this.entity = entity;
        }

        public List<SetWindowEntity> getEntity() {
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
