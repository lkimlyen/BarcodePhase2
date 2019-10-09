package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import rx.Subscriber;

public class GroupProductDetailUsecase extends BaseUseCase<BaseResponse<String>> {
    private static final String TAG = GroupProductDetailUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public GroupProductDetailUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable<BaseResponse<String>> buildUseCaseObservable() {
        String json = ((RequestValue) requestValues).json;
        return remoteRepository.groupProductDetail(Constants.KEY,json);
    }



    @Override
    protected DisposableObserver<BaseResponse<String>> buildUseCaseSubscriber() {
        return new DefaultObserver<BaseResponse<String>>() {
            @Override
            public void onNext(BaseResponse<String> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data));
                if (useCaseCallback != null) {
                    String result = data.getData();
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
