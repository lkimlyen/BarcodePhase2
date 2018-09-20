package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;

import rx.Observable;
import rx.Subscriber;

public class UpdateProductDetailGroupUsecase extends BaseUseCase {
    private static final String TAG = UpdateProductDetailGroupUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public UpdateProductDetailGroupUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
         int masterGroupId = ((RequestValue) requestValues).masterGroupId;
        String jsonNew = ((RequestValue) requestValues).jsonNew;
        String jsonUpdate = ((RequestValue) requestValues).jsonUpdate;
        String jsonDelete = ((RequestValue) requestValues).jsonDelete;
        int userId = ((RequestValue) requestValues).userId;
        return remoteRepository.updateProductDetailGroup(Constants.KEY, masterGroupId,jsonNew,jsonUpdate,jsonDelete,userId);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<BaseResponse>() {
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
        };
    }

    public static final class RequestValue implements RequestValues {
        private final int masterGroupId;
        private final String jsonNew;
        private final String jsonUpdate;
        private final String jsonDelete;
        private final int userId;

        public RequestValue(int masterGroupId, String jsonNew, String jsonUpdate, String jsonDelete, int userId) {
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
