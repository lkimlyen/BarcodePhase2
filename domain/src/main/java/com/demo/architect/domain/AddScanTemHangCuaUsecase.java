package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class AddScanTemHangCuaUsecase extends BaseUseCase<BaseResponse<Integer>> {
    private static final String TAG = AddScanTemHangCuaUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public AddScanTemHangCuaUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse<Integer>> buildUseCaseObservable() {
        long orderId = ((RequestValue) requestValues).orderId;
        long productSetId = ((RequestValue) requestValues).productSetId;
        int direction = ((RequestValue) requestValues).direction;
        String packCode = ((RequestValue) requestValues).packCode;
        int numberOnPack = ((RequestValue) requestValues).numberOnPack;

        long userId = ((RequestValue) requestValues).userId;
        String json = ((RequestValue) requestValues).json;
        return remoteRepository.addScanTemHangCua(Constants.KEY, orderId,productSetId,direction,packCode,
                numberOnPack,userId,json );
    }


    @Override
    protected DisposableObserver<BaseResponse<Integer>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseResponse<Integer>>() {
            @Override
            public void onNext(BaseResponse<Integer> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    int result = data.getData();
                    if (result > 0 && data.getStatus() == 1) {
                        useCaseCallback.onSuccess(new ResponseValue(result));
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

        private final long orderId;
        private final long productSetId;
        private final int direction;
        private final String packCode;
        private final int numberOnPack;
        private final long userId;
        private final String json;

        public RequestValue(long orderId, long productSetId, int direction, String packCode, int numberOnPack, long userId, String json) {
            this.orderId = orderId;
            this.productSetId = productSetId;
            this.direction = direction;
            this.packCode = packCode;
            this.numberOnPack = numberOnPack;
            this.userId = userId;
            this.json = json;
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
