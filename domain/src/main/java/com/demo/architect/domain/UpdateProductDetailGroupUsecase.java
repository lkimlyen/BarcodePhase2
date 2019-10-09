package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class UpdateProductDetailGroupUsecase extends BaseUseCase<BaseResponse> {
    private static final String TAG = UpdateProductDetailGroupUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public UpdateProductDetailGroupUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse> buildUseCaseObservable() {
        long masterGroupId = ((RequestValue) requestValues).masterGroupId;
        String jsonNew = ((RequestValue) requestValues).jsonNew;
        String jsonUpdate = ((RequestValue) requestValues).jsonUpdate;
        String jsonDelete = ((RequestValue) requestValues).jsonDelete;
        long userId = ((RequestValue) requestValues).userId;
        return remoteRepository.updateProductDetailGroup(Constants.KEY, masterGroupId,jsonNew,jsonUpdate,jsonDelete,userId);
    }


    @Override
    protected DisposableObserver<BaseResponse> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    String result = data.getDescription();
                    if (result != null && data.getStatus() == 1) {
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
        private final long masterGroupId;
        private final String jsonNew;
        private final String jsonUpdate;
        private final String jsonDelete;
        private final long userId;

        public RequestValue(long masterGroupId, String jsonNew, String jsonUpdate, String jsonDelete, long userId) {
            this.masterGroupId = masterGroupId;
            this.jsonNew = jsonNew;
            this.jsonUpdate = jsonUpdate;
            this.jsonDelete = jsonDelete;
            this.userId = userId;
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
