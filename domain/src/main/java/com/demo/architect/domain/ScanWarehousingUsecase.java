package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class ScanWarehousingUsecase extends BaseUseCase<BaseResponse> {
    private static final String TAG = ScanWarehousingUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public ScanWarehousingUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse> buildUseCaseObservable() {
        long userId = ((RequestValue) requestValues).userId;
        long orderId = ((RequestValue) requestValues).orderId;
        String phone = ((RequestValue) requestValues).phone;
        String date = ((RequestValue) requestValues).date;
        String json = ((RequestValue) requestValues).json;
        return remoteRepository.scanWarehousing("13AKby8uFhdlayHD6oPsaU90b8o00=", userId, orderId,
                phone, date,json);
    }


    @Override
    protected DisposableObserver<BaseResponse> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    if (data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue());
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
        private final long userId;
        private final long orderId;
        private final String phone;
        private final String date;
        private final String json;

        public RequestValue(long userId, long orderId, String phone, String date, String json) {
            this.userId = userId;
            this.orderId = orderId;
            this.phone = phone;
            this.date = date;
            this.json = json;
        }

    }

    public static final class ResponseValue implements ResponseValues {


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
