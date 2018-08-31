package com.demo.architect.domain;

import android.util.Log;

import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.HistoryEntity;
import com.demo.architect.data.model.ProductPackagingEntity;
import com.demo.architect.data.repository.base.order.remote.OrderRepository;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class GetListPrintPackageHistoryUsecase extends BaseUseCase {
    private static final String TAG = GetListPrintPackageHistoryUsecase.class.getSimpleName();
    private final OrderRepository remoteRepository;

    public GetListPrintPackageHistoryUsecase(OrderRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        int orderId = ((RequestValue) requestValues).orderId;
        int productId = ((RequestValue) requestValues).productId;
        int apartmentId = ((RequestValue) requestValues).apartmentId;
        String packCode = ((RequestValue) requestValues).packCode;
        String sttPack = ((RequestValue) requestValues).sttPack;
        return remoteRepository.getListPrintPackageHistory(orderId, productId, apartmentId,packCode,sttPack);
    }

    @Override
    protected Subscriber buildUseCaseSubscriber() {
        return new Subscriber<BaseListResponse<HistoryEntity>>() {
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
            public void onNext(BaseListResponse<HistoryEntity> data) {
                Log.d(TAG, "onNext: " + String.valueOf(data.getStatus()));
                if (useCaseCallback != null) {
                    List<HistoryEntity> result = data.getData();
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
        private final int orderId;
        private final int productId;
        private final int apartmentId;
        private final String packCode;
        private final String sttPack;


        public RequestValue(int orderId, int productId, int apartmentId, String packCode, String sttPack) {
            this.orderId = orderId;
            this.productId = productId;
            this.apartmentId = apartmentId;
            this.packCode = packCode;
            this.sttPack = sttPack;
        }

    }

    public static final class ResponseValue implements ResponseValues {
        private List<HistoryEntity> entity;

        public ResponseValue(List<HistoryEntity> entity) {
            this.entity = entity;
        }

        public List<HistoryEntity> getEntity() {
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
