package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class ScanProductDetailOutUsecase extends BaseUseCase<BaseResponse<Integer>>  {
    private static final String TAG = ScanProductDetailOutUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public ScanProductDetailOutUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse<Integer>>  buildUseCaseObservable() {
        String json = ((RequestValue) requestValues).json;
        return remoteRepository.scanProductDetailOut("13AKby8uFhdlayHD6oPsaU90b8o00=",json);
    }

    @Override
    protected DisposableObserver<BaseResponse<Integer>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseResponse<Integer>>() {
            @Override
            public void onNext(BaseResponse<Integer> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    if (data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue(data.getData()));
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
        private final String json;

        public RequestValue(String json) {
            this.json = json;
        }

    }

    public static final class ResponseValue implements ResponseValues {
        private int id;

        public ResponseValue(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
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
