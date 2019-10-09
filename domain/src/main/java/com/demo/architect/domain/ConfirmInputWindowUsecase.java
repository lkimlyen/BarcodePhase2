package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class ConfirmInputWindowUsecase extends BaseUseCase<BaseListResponse> {
    private static final String TAG = ConfirmInputWindowUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public ConfirmInputWindowUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseListResponse> buildUseCaseObservable() {
        int departmentId = ((RequestValue) requestValues).departmentId;
        long userId = ((RequestValue) requestValues).userId;
        String json = ((RequestValue) requestValues).json;
        return remoteRepository.confirmInputWindow(Constants.KEY,departmentId,userId, json);
    }


    @Override
    protected DisposableObserver<BaseListResponse> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseListResponse>() {
            @Override
            public void onNext(BaseListResponse data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    if (data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue(data.getDescription()));
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
        private final int departmentId;
        private final long userId;
        private final String json;

        public RequestValue(int departmentId, long userId, String json) {
            this.departmentId = departmentId;
            this.userId = userId;
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
