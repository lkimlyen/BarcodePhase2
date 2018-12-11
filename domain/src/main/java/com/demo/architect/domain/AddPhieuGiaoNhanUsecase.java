package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;

import rx.Observable;
import rx.Subscriber;

public class AddPhieuGiaoNhanUsecase extends BaseUseCase {
    private static final String TAG = AddPhieuGiaoNhanUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public AddPhieuGiaoNhanUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {

        long orderId = ((RequestValue) requestValues).orderId;
        int departmentInId = ((RequestValue) requestValues).departmentInID;
        int departmentOutId = ((RequestValue) requestValues).departmentOutID;
        int number = ((RequestValue) requestValues).number;
        String data = ((RequestValue) requestValues).data;
        long userId = ((RequestValue) requestValues).userId;
        return remoteRepository.addPhieuGiaoNhan(Constants.KEY, orderId, departmentInId, departmentOutId, number, data, userId);
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
                    int result = data.getData();
                    if (result > 0 && data.getStatus() == 1) {
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
        private final int departmentInID;
        private final int departmentOutID;
        private final int number;
        private final String data;
        private final long userId;

        public RequestValue(long orderId, int departmentInID, int departmentOutID, int number, String data, long userId) {
            this.orderId = orderId;
            this.departmentInID = departmentInID;
            this.departmentOutID = departmentOutID;
            this.number = number;
            this.data = data;
            this.userId = userId;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private long id;

        public ResponseValue(long id) {
            this.id = id;
        }

        public long getId() {
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
