package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class PostListCodeProductDetailUsecase extends BaseUseCase {
    private static final String TAG = PostListCodeProductDetailUsecase.class.getSimpleName();
    private final ProductRepository remoteRepository;

    public PostListCodeProductDetailUsecase(ProductRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        String json = ((RequestValue) requestValues).json;
        int userId = ((RequestValue) requestValues).userId;
        String note = ((RequestValue) requestValues).note;
        return remoteRepository.postListCodeProductDetail(Constants.KEY, json, userId, note);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<BaseListResponse<ProductGroupEntity>>() {
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
            public void onNext(BaseListResponse<ProductGroupEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<ProductGroupEntity> result = data.getData();
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
        private final int userId;
        private final String note;

        public RequestValue(String json, int userId, String note) {
            this.json = json;
            this.userId = userId;
            this.note = note;
        }
    }

    public static final class ResponseValue implements ResponseValues {
        private List<ProductGroupEntity> entity;

        public ResponseValue(List<ProductGroupEntity> entity) {
            this.entity = entity;
        }

        public List<ProductGroupEntity> getEntity() {
            return entity;
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
