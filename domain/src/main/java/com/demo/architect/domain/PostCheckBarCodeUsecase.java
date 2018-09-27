package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.ModuleEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class PostCheckBarCodeUsecase extends BaseUseCase {
    private static final String TAG = PostCheckBarCodeUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public PostCheckBarCodeUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        long orderId = ((RequestValue) requestValues).orderId;
        int productId = ((RequestValue) requestValues).productId;
        int apartmentId = ((RequestValue) requestValues).apartmentId;
        String packCode = ((RequestValue) requestValues).packCode;
        String sttPack = ((RequestValue) requestValues).sttPack;
        String code = ((RequestValue) requestValues).code;
        return remoteRepository.postCheckBarCode(orderId, productId, apartmentId,packCode,sttPack,code);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<BaseListResponse<ProductPackagingEntity>>() {
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
            public void onNext(BaseListResponse<ProductPackagingEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<ProductPackagingEntity> result = data.getData();
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
        private final long orderId;
        private final int productId;
        private final int apartmentId;
        private final String packCode;
        private final String sttPack;
        private final String code;


        public RequestValue(long orderId, int productId, int apartmentId, String packCode, String sttPack, String code) {
            this.orderId = orderId;
            this.productId = productId;
            this.apartmentId = apartmentId;
            this.packCode = packCode;
            this.sttPack = sttPack;
            this.code = code;
        }

    }

    public static final class ResponseValue implements ResponseValues {
        private List<ProductPackagingEntity> entity;

        public ResponseValue(List<ProductPackagingEntity> entity) {
            this.entity = entity;
        }

        public List<ProductPackagingEntity> getEntity() {
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
