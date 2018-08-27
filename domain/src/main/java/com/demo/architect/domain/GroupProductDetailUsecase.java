package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class GroupProductDetailUsecase extends BaseUseCase {
    private static final String TAG = GroupProductDetailUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public GroupProductDetailUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        String json = ((RequestValue) requestValues).json;
        return remoteRepository.groupProductDetail(Constants.KEY,json);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<BaseResponse<String>>() {
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
            public void onNext(BaseResponse<String> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    String result = data.getData();
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
        private final String json;

        public RequestValue(String json) {
            this.json = json;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private String groupCode;

        public ResponseValue(String groupCode) {
            this.groupCode = groupCode;
        }

        public String getGroupCode() {
            return groupCode;
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
