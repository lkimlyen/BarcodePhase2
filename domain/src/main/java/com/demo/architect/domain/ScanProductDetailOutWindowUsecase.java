package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import rx.Observable;
import rx.Subscriber;

public class ScanProductDetailOutWindowUsecase extends BaseUseCase {
    private static final String TAG = ScanProductDetailOutWindowUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public ScanProductDetailOutWindowUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        long orderId = ((RequestValue) requestValues).orderId;
        int departmentOut = ((RequestValue) requestValues).departmentOut;
        int departmentIn = ((RequestValue) requestValues).departmentIn;
        long userId = ((RequestValue) requestValues).userId;
        int staffId = ((RequestValue) requestValues).staffId;
        String json = ((RequestValue) requestValues).json;
        return remoteRepository.scanProductDetailOutWindow("13AKby8uFhdlayHD6oPsaU90b8o00=",
                orderId, departmentOut, departmentIn, userId, staffId, json);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<BaseResponse<Integer>>() {
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
            public void onNext(BaseResponse<Integer> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    if (data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue(data.getData()));
                    } else {
                        useCaseCallback.onError(new ErrorValue(data.getDescription()));
                    }
                }
            }
        };
    }

    public static final class RequestValue implements RequestValues {
        private final long orderId;
        private final int departmentOut;
        private final int departmentIn;
        private final long userId;
        private final int staffId;
        private final String json;

        public RequestValue(long orderId, int departmentOut, int departmentIn, long userId, int staffId, String json) {
            this.orderId = orderId;
            this.departmentOut = departmentOut;
            this.departmentIn = departmentIn;
            this.userId = userId;
            this.staffId = staffId;
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
