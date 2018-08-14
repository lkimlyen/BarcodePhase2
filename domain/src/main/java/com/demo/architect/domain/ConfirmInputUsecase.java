package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import rx.Observable;
import rx.Subscriber;

public class ConfirmInputUsecase extends BaseUseCase {
    private static final String TAG = ConfirmInputUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public ConfirmInputUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        int departmentId = ((RequestValue) requestValues).departmentId;
        String json = ((RequestValue) requestValues).json;
        return remoteRepository.confirmInput(departmentId, json);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<BaseListResponse>() {
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
            public void onNext(BaseListResponse data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    if (data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue(data.getDescription()));
                    } else {
                        useCaseCallback.onError(new ErrorValue(data.getDescription()));
                    }
                }
            }
        };
    }

    public static final class RequestValue implements RequestValues {
        private final int departmentId;
        private final String json;

        public RequestValue(int departmentId, String json) {
            this.departmentId = departmentId;
            this.json = json;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private String description;

        public ResponseValue(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
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
